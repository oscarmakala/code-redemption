package com.divforce.cr.orderservice.sagaparticipants;

import com.divforce.cr.voucherservice.api.*;
import com.divforce.cr.voucherservice.api.command.FulFillReservationCommand;
import com.divforce.cr.voucherservice.api.command.RejectReservationCommand;
import com.divforce.cr.voucherservice.api.command.ReserveVoucherCommand;
import com.divforce.cr.voucherservice.api.command.ValidateVoucherCommand;
import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Oscar Makala
 */
@Service
public class VoucherServiceProxy {
    public final CommandEndpoint<ValidateVoucherCommand> validate = CommandEndpointBuilder
            .forCommand(ValidateVoucherCommand.class)
            .withChannel(VoucherServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();

    public final CommandEndpoint<ReserveVoucherCommand> reserve = CommandEndpointBuilder
            .forCommand(ReserveVoucherCommand.class)
            .withChannel(VoucherServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();
    public final CommandEndpoint<RejectReservationCommand> reject = CommandEndpointBuilder
            .forCommand(RejectReservationCommand.class)
            .withChannel(VoucherServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();

    public final CommandEndpoint<FulFillReservationCommand> fulfill = CommandEndpointBuilder
            .forCommand(FulFillReservationCommand.class)
            .withChannel(VoucherServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();
}
