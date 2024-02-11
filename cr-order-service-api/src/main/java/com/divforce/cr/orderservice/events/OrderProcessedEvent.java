package com.divforce.cr.orderservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Oscar Makala
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderProcessedEvent implements OrderDomainEvent {
    private String orderId;
    private String mobileNumber;
    private BigDecimal amount;
}
