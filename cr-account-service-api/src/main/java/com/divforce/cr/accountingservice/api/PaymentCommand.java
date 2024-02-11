package com.divforce.cr.accountingservice.api;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCommand implements Command {
    private String transactionId;
}
