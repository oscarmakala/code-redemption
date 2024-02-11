package com.divforce.cr.voucherservice.exception;

/**
 * @author Oscar Makala
 */
public class VoucherAlreadyExistsException extends RuntimeException {
    public VoucherAlreadyExistsException(String code) {
        super("Voucher with code " + code + " Already exits");
    }
}
