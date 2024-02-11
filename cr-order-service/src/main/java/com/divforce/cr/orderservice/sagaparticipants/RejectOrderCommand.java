package com.divforce.cr.orderservice.sagaparticipants;

import com.divforce.cr.common.RejectionReason;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RejectOrderCommand extends OrderCommand {
    private RejectionReason rejectionReason;
    public RejectOrderCommand(String orderId, RejectionReason rejectionReason) {
        super(orderId);
        this.rejectionReason = rejectionReason;
    }
}
