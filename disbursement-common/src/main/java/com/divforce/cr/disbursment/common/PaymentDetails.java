package com.divforce.cr.disbursment.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
    private String transactionId;
    private String mobile;
    private BigDecimal amount;
}
