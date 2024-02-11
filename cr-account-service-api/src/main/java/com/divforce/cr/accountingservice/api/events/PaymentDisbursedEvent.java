package com.divforce.cr.accountingservice.api.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDisbursedEvent implements PaymentDomainEvent {
    private String mobile;
    private String externalTransactionId;
    private String thirdPartyStatus;
    private String code;
    private Date completedAt;

}
