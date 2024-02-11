package com.divforce.cr.orderservice.domain;

import lombok.Getter;

@Getter
public class VoucherAlreadyUseException extends RuntimeException {
    private final String voucherCode;

    public VoucherAlreadyUseException(String voucherCode) {
        super(voucherCode);
        this.voucherCode = voucherCode;
    }
}
