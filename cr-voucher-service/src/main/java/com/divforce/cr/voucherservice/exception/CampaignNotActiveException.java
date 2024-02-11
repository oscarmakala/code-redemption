package com.divforce.cr.voucherservice.exception;

/**
 * @author Oscar Makala
 */
public class CampaignNotActiveException extends RuntimeException {
    public CampaignNotActiveException(String serialNumber) {
        super(serialNumber);
    }
}
