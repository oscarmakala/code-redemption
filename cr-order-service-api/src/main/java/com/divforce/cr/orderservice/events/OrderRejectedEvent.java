package com.divforce.cr.orderservice.events;

import com.divforce.cr.common.RejectionReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRejectedEvent implements OrderDomainEvent {
    private String orderId;
    private String mobileNumber;
    private RejectionReason rejectionReason;
}
