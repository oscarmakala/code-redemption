package com.divforce.cr.orderservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private String orderId;
    private String serialNumber;
    private String mobileNumber;
    private BigDecimal amount;
    private String code;
    private String keyPhrase;
    private Integer redeemedVouchers;

}
