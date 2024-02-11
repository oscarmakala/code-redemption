package com.divforce.cr.accountingservice.domain;

/**
 * @author Oscar Makala
 */
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String transactionId) {
        super("Payment not found with " + transactionId);
    }
}
