package com.divforce.cr.cqrs.voucherhistory.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherHistoryAggregate {
    private @Id
    String voucherState;
    private BigDecimal totalAmount;
    private Integer totalCount;

}
