package com.divforce.cr.orderservice.sagaparticipants;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
public class SubmitOrderCommand extends OrderCommand {
    public SubmitOrderCommand(String orderId) {
        super(orderId);
    }
}
