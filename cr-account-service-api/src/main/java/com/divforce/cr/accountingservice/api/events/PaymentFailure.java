package com.divforce.cr.accountingservice.api.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailure {
    private String thirdPartyTransactionId;
    private String externalDataOne;
    private String thirdPartyStatus;
    private String errorCode;
    private String errorMessage;
}
