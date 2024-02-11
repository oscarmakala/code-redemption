package com.divforce.cr.orderservice.sagaparticipants;

import com.divforce.cr.accountingservice.api.*;
import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Oscar Makala
 */
@Service
public class AccountServiceProxy {
    public final CommandEndpoint<CreatePaymentCommand> create = CommandEndpointBuilder
            .forCommand(CreatePaymentCommand.class)
            .withChannel(AccountServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();

    public CommandEndpoint<PaymentCommand> disburse = CommandEndpointBuilder
            .forCommand(PaymentCommand.class)
            .withChannel(AccountServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();

    public CommandEndpoint<RejectPaymentCommand> cancel = CommandEndpointBuilder
            .forCommand(RejectPaymentCommand.class)
            .withChannel(AccountServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();
    public CommandEndpoint<RejectPaymentCommand> reject = CommandEndpointBuilder
            .forCommand(RejectPaymentCommand.class)
            .withChannel(AccountServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();
}
