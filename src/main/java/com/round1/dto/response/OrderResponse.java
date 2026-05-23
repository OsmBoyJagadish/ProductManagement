package com.round1.dto.response;

import com.round1.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
}