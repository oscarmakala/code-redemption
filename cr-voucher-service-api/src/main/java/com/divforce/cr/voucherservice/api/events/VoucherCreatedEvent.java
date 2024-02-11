package com.divforce.cr.voucherservice.api.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherCreatedEvent implements VoucherDomainEvent {
    private String serialNumber;
    private BigDecimal amount;
    private String currency;
    private Date expireAt;
    private String code;
    private Long campaignId;
    private String keyPhrase;

}
