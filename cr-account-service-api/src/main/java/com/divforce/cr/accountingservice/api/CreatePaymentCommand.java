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
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentCommand implements Command {
    private String serialNumber;
    private String transactionId;
    private String mobile;
    private BigDecimal amount;
    private String code;
    private String passPhrase;
}

