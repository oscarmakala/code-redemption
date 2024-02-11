package com.divforce.cr.voucherservice.exception;

/**
 * @author Oscar Makala
 */
public class NoSuchCampaignException extends RuntimeException {
    public NoSuchCampaignException(Long campaignId) {
        super("Campaign with id " + campaignId + " not found");
    }
}
