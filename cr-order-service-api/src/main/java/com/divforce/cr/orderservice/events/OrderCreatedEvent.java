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
public class OrderCreatedEvent implements OrderDomainEvent {
    
    private String orderId;
    private String voucherId;
    private String msisdn;
    private BigDecimal amount;
}
