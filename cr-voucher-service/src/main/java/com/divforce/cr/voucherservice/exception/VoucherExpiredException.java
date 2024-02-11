package com.divforce.cr.voucherservice.exception;

/**
 * @author Oscar Makala
 */
public class VoucherExpiredException extends RuntimeException {
    public VoucherExpiredException(String code) {
        super(code);
    }
}
