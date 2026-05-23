package com.round1.exception;

public class ProductNotActiveException extends RuntimeException {
    public ProductNotActiveException(String productName) {
        super("Product '" + productName + "' is currently not available.");
    }
}