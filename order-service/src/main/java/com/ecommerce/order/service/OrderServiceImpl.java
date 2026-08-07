package com.ecommerce.order.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.ecommerce.order.dto.PaymentResultRequest;
import com.ecommerce.order.dto.PaymentResultStatus;
import com.ecommerce.order.client.InventoryClient;
import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.ProductInfoResponse;
import com.ecommerce.order.entity.CustomerOrder;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.exception.InvalidOrderStatusException;
import com.ecommerce.order.exception.OrderNotFoundException;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;  
    private PaymentResultStatus paymentStatus;

    //Skapar en nu order
    @Override
    public OrderResponse createOrder(Long userId,OrderRequest request) {
        //först hämtar vi korrekt namn och pris från produkt-service, ingen lagerreservations gjorts ännu
        List<OrderItem> items = request.getItems()
                .stream()
                .map(itemRequest -> {
                    ProductInfoResponse product =
                        productClient.getProductById(itemRequest.getProductId());
                    return OrderMapper.toOrderItem(itemRequest, product);
                })
                .toList();
        //Här sparar vi vilka orderrader som faktiskt har reserverats
        //Om en senare reservation misslyckas kan dessa släppas tillbaka
        List<OrderItem> reservedItems = new ArrayList<>();
        CustomerOrder saveOrder;
        try {
            for(OrderItem item : items){
                inventoryClient.reseveInventory(
                    item.getProductId(), 
                    item.getQuantity());
            reservedItems.add(item);
            }
            
            CustomerOrder order = OrderMapper.toEntity(userId, items);
            saveOrder = orderRepository.save(order);
            
        } catch (RuntimeException exception) {
            //När något musslyckades efter att en eller felra produkter redan har reserverats
            releaseInventoryQuietly(reservedItems);
            throw exception;
        }
        return OrderMapper.toResponse(saveOrder);
        
    }

    //Hämtar alla orders
    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    //Hämta order för en specifik id
    @Override
    public OrderResponse getOrderById(Long id, Long userId) {
        CustomerOrder order = orderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderMapper.toResponse(order);
    }

    //Hämta alla orders för en användare
    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }
    
    public OrderResponse cancelOrder(Long id, Long userId){
        CustomerOrder order = orderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new OrderNotFoundException(id));
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException(
                "Only orders with status PENDING can be cancelled"
            );
        }

        //Vi hålelr reda på vilka orderrader som faktiskt har släppts
        //Om ett senare steg misslyckas försöker vi reservera dem igen
        List<OrderItem> releasedItems = new ArrayList<>();
        CustomerOrder updatedOrder;

        try {
            for(OrderItem item : order.getItems()) {
                inventoryClient.releaseInventory(
                    item.getProductId(), 
                item.getQuantity());
                releasedItems.add(item);
            }
            order.setStatus(OrderStatus.CANCELLED);
            updatedOrder = orderRepository.save(order);
        } catch (RuntimeException exception) {
            //Försöker återställa tidigare lagerreserveationer
            reserveInventoryQuietly(releasedItems);
            throw exception;
        }
        return OrderMapper.toResponse(updatedOrder);
    }
    /**
     * Släpper tidigare reserverat lager.
     *
     * Metoden används som kompensation när orderskapandet misslyckas.
     * Ett kompensationsfel loggas utan att det ursprungliga felet ersätts.
     */
    private void releaseInventoryQuietly(List<OrderItem> reservedItems){
        for(OrderItem item : reservedItems){
            try {
                inventoryClient.releaseInventory(
                    item.getProductId(), 
                    item.getQuantity());
            } catch (RuntimeException compensationException) {
                log.error(
                    "Failed to release inventory for product id {} during compensation",
                    item.getProductId(),
                    compensationException
                );
            }
        }
    }

    /**
     * Försöker reservera lager igen.
     *
     * Metoden används som kompensation om cancel-operationen misslyckas
     * efter att lager redan har släppts.
     */
    private void reserveInventoryQuietly(List<OrderItem> releasedItems) {
        for(OrderItem item : releasedItems){
            try {
                inventoryClient.reseveInventory(
                    item.getProductId(), 
                    item.getQuantity());
            } catch (RuntimeException compensationException) {
                log.error(
                    "Failed to restore inventory reservation for product id {}",
                    item.getProductId(),
                    compensationException
                );
            }
        }
    }
    /**
     * Hanterar betalningsresultatet.
     *
     * COMPLETED:
     * - bekräftar reserverat lager
     * - ändrar orderstatus till CONFIRMED
     *
     * FAILED:
     * - släpper reserverat lager
     * - ändrar orderstatus till PAYMENT_FAILED
     */
    @Override
    public OrderResponse handlePaymentResult(
        Long orderId,
        Long userId,
        PaymentResultRequest request
    ){
        CustomerOrder order = orderRepository
            .findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException(
                "Only orders with status PENDING can receive a payment result"
            );
        }

        if (request.getPaymentStatus() == PaymentResultStatus.COMPLETED) {
            confirmOrderInventory(order);
            order.setStatus(OrderStatus.CONFIRMED);
            
        }else if(request.getPaymentStatus() == PaymentResultStatus.FAILED){
            releaseOrderInventory(order);
            order.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        CustomerOrder updatedOrder = orderRepository.save(order);
        return OrderMapper.toResponse(updatedOrder);

    }

    //Bekräftar alla reserverade produkter or ordern
    private void confirmOrderInventory(CustomerOrder order){
        
        for(OrderItem item: order.getItems()){
            inventoryClient.confirmInventory(item.getProductId(), item.getQuantity());
        }
    }

    /**
     * Släpper orderns reservationer vid misslyckad betalning.
     *
     * Om en senare release misslyckas försöker vi återställa
     * tidigare släppta reservationer.
     */
    private void releaseOrderInventory(CustomerOrder order){

        List<OrderItem> releasedItems = new ArrayList<>();
        try {
            for(OrderItem item : order.getItems()){
                inventoryClient.releaseInventory(item.getProductId(), item.getQuantity());
                releasedItems.add(item);
            }
        } catch (RuntimeException exception) {
            reserveInventoryQuietly(releasedItems);
            throw exception;
        }
    
    }
}
