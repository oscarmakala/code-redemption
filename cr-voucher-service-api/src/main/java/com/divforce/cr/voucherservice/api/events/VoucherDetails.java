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
public class VoucherDetails {
    private String code;
    private BigDecimal amount;
    private String currency;
    private Date expireAt;
    private Long campaignId;
}
