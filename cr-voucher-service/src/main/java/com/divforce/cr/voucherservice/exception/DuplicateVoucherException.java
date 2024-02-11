package com.divforce.cr.voucherservice.exception;

/**
 * @author Oscar Makala
 */
public class DuplicateVoucherException extends RuntimeException {
    public DuplicateVoucherException(int size) {
        super(size + " duplicate voucher codes");
    }
}
