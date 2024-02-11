package com.divforce.cr.orderservice.domain;

/**
 * @author Oscar Makala
 */
public class NoSuchVoucherException extends RuntimeException {
    public NoSuchVoucherException(String voucherCode) {
        super("Voucher not found with code " + voucherCode);
    }
}
