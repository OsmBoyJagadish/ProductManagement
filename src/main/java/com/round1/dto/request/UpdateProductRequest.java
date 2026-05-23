package com.round1.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}