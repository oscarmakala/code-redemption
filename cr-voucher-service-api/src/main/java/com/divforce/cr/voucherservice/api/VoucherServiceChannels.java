package com.divforce.cr.voucherservice.api;

/**
 * @author Oscar Makala
 */
public class VoucherServiceChannels {
    private VoucherServiceChannels() {
        super();
    }

    public static final String COMMAND_CHANNEL = "voucherService";
    public static final String EVENT_CHANNEL = "com.divforce.cr.voucherservice.domain.Voucher";
}
