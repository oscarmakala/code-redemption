package com.divforce.cr.accountingservice.api;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectPaymentCommand implements Command {
    private String transactionId;
    private String thirdPartyTransactionId;
    private String externalDataOne;
    private String thirdPartyStatus;
    private String errorCode;
    private String errorMessage;
}
