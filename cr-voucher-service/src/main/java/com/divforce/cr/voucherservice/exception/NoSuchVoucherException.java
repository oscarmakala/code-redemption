package com.divforce.cr.voucherservice.exception;

/**
 * @author Oscar Makala
 */
public class NoSuchVoucherException extends RuntimeException {
    public NoSuchVoucherException(String voucherId) {
        super("Voucher not found: " + voucherId);
    }
}
