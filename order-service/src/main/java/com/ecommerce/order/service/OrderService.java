package com.ecommerce.order.service;

import java.util.List;

import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.PaymentResultRequest;

public interface OrderService {

    OrderResponse createOrder (Long userId, OrderRequest request);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id, Long userId);
    List<OrderResponse> getOrdersByUserId(Long userId);
    OrderResponse cancelOrder(Long id, Long userId);
    OrderResponse handlePaymentResult(Long orderId, Long userId, PaymentResultRequest request);
}
