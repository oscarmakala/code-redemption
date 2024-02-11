package com.divforce.cr.accountingservice.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Oscar Makala
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentFailedException extends RuntimeException {
    private String errorMessage;
    private String errorCode;
    private String thirdPartyTransactionId;
    private String externalDataOne;
    private String thirdPartyStatus;

    public PaymentFailedException(String message) {
        super(message);
    }

    public PaymentFailedException(String transactionStatus,
                                  String externalTxnOne,
                                  String externalTxnTwo,
                                  String errorCode,
                                  String message) {
        super(transactionStatus);
        this.thirdPartyTransactionId = externalTxnOne;
        this.externalDataOne = externalTxnTwo;
        this.thirdPartyStatus = transactionStatus;
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
}
