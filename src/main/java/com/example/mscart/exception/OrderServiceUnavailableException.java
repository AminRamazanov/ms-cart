package com.example.mscart.exception;

public class OrderServiceUnavailableException extends RuntimeException {
    public OrderServiceUnavailableException(String message) {
        super(message);
    }
}
