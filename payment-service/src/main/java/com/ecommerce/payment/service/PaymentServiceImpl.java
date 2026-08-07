package com.ecommerce.payment.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ecommerce.payment.client.OrderClient;
import com.ecommerce.payment.dto.OrderDetailsResponse;
import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.entity.PaymentStatus;
import com.ecommerce.payment.exception.OrderNotPayableException;
import com.ecommerce.payment.exception.PaymentAlreadyCompletedException;
import com.ecommerce.payment.exception.PaymentNotFoundException;
import com.ecommerce.payment.mapper.PaymentMapper;
import com.ecommerce.payment.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

//Implementation av betalningslogiken
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    /**
     * Används för vår tillfälliga betalningssimulering.
     *
     * Tillåtna värden:
     * COMPLETED
     * FAILED
     */
    @Value("${payment.simulation.result:COMPLETED}")
    private String simulationResult;

    //Skapar och behandlar en betalning
    @Override
    @Transactional
    public PaymentResponse processPayment(
        Long userId,
        PaymentRequest request
    ){
        Long orderId = request.getOrderId();

        //En order som redan har en lyckad betalning ska inte kunna betalas igen
        if (paymentRepository.existsByOrderIdAndStatus(orderId, PaymentStatus.COMPLETED)) {
            throw new PaymentAlreadyCompletedException(orderId);
        }

        /*
         * Hämta den riktiga ordern.
         *
         * OrderClient skickar med X-User-Id, vilket gör att
         * användaren endast kan komma åt sin egen order.
         */
        OrderDetailsResponse order = orderClient.getOrderById(orderId, userId);
        validateOrder(order, userId, orderId);

        /*
         * Beloppet hämtas från order-service.
         *
         * Klienten får alltså inte själv välja vilket
         * belopp som ska betalas.
         */
        BigDecimal amount = order.getTotalAmount();
        Payment payment = PaymentMapper.toEntity(request, userId, amount);

        //Payment skapas först som PENDING
        Payment savedPayment = paymentRepository.save(payment);

        //Sedan smiulerar vi betalningsleverantörens resultat;
        PaymentStatus finalStatus = determineSimulatedStatus();
        savedPayment.setStatus(finalStatus);
        Payment processedPayment = paymentRepository.save(savedPayment);
        return PaymentMapper.toResponse(processedPayment);

    }

    //Hämtar en betalning endast om den tillhör användaren
    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId, Long userId){
        Payment payment = paymentRepository
                .findByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return PaymentMapper.toResponse(payment);
    }

    //Hämtar användaren betalningar
    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getMyPayments(Long userId){
        return paymentRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    //Kontrollerar att ordern är gilgit för betalning
    private void validateOrder(OrderDetailsResponse order, Long userId, Long requestedOrderId) {

        if (order.getId() == null || !Objects.equals(order.getId(), requestedOrderId)) {
            throw new OrderNotPayableException(
                "order-service returned invalid order"
            );
        }

        if (order.getUserId() == null || !Objects.equals(order.getUserId(), userId)) {
            throw new OrderNotPayableException(
                "Ordeer does not belong to the current user"
            );
        }

        if (order.getStatus() == null || !order.getStatus().equalsIgnoreCase("PENDING")) {
            throw new OrderNotPayableException(
                "Only orders with status PENDING can be paid"
            );
        }

        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderNotPayableException(
                "Order amount must be greater than zero"
            );
        }
    }
    
    //Läser vilket resultat betalningssimuleringen ska ge
    private PaymentStatus determineSimulatedStatus(){
        if ("COMPLETED".equalsIgnoreCase(simulationResult)) {
            return PaymentStatus.COMPLETED;
        }
        if ("FAILED".equalsIgnoreCase(simulationResult)) {
            return PaymentStatus.FAILED;
        }
        throw new IllegalArgumentException(
            "payment.simulation.result must be COMPLETED or FAILED"
        );
    }
}
