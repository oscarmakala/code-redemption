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
public class ValidateVoucherCommand implements Command {
    private String serialNumber;
    private String orderId;
    private Integer redeemedVoucher;
}
