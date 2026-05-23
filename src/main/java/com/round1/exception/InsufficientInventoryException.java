package com.round1.exception;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(String productName, int requested, int available) {
        super("Insufficient inventory for product '" + productName +
              "'. Requested: " + requested + ", Available: " + available);
    }
}