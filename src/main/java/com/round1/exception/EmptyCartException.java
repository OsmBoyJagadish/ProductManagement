package com.round1.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Cannot place order: your cart is empty.");
    }
}