package com.divforce.cr.accountingservice.api;

/**
 * @author Oscar Makala
 */
public final class AccountServiceChannels {
    private AccountServiceChannels() {
        super();
    }

    public static final String COMMAND_CHANNEL = "accountService";
    public static final String EVENT_CHANNEL = "com.divforce.cr.accountingservice.domain.Payment";
}
