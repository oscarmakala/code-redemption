package com.divforce.cr.orderservice.domain;

/**
 * @author Oscar Makala
 */
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("Order not found" + orderId);
    }
}
