package com.divforce.cr.voucherservice.api.command;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FulFillReservationCommand implements Command {
    private String serialNumber;
}
