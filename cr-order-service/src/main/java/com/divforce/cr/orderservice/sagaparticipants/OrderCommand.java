package com.divforce.cr.orderservice.sagaparticipants;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCommand implements Command {
    private String orderId;

}
