package com.divforce.cr.voucherservice.exception;

/**
 * @author Oscar Makala
 */
public class VoucherAlreadyUsedException  extends RuntimeException{
    public VoucherAlreadyUsedException(String code) {
        super("");
    }
}
