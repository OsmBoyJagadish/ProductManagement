package com.round1.services;

import com.round1.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(String email);
    List<OrderResponse> getMyOrders(String email);
    OrderResponse getOrderById(String email, Long orderId);
}
