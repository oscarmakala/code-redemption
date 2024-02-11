package com.divforce.cr.voucherservice.exception;

public class VoucherNotUploadedException extends RuntimeException {

    public VoucherNotUploadedException(Long campaignId) {
        super("Campaign " + campaignId + " has no vouchers uploaded");
    }
}
