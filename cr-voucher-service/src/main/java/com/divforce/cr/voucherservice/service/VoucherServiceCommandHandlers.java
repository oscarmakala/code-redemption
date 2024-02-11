package com.divforce.cr.voucherservice.service;

import com.divforce.cr.common.RejectionReason;
import com.divforce.cr.common.UnsupportedStateTransitionException;
import com.divforce.cr.voucherservice.api.*;
import com.divforce.cr.voucherservice.api.command.*;
import com.divforce.cr.voucherservice.domain.*;
import com.divforce.cr.voucherservice.exception.*;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class VoucherServiceCommandHandlers {
    private final VoucherService voucherService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder.fromChannel(VoucherServiceChannels.COMMAND_CHANNEL)
                .onMessage(ValidateVoucherCommand.class, this::validateVoucher)
                .onMessage(ReserveVoucherCommand.class, this::reserveVoucher)
                .onMessage(RejectReservationCommand.class, this::rejectReservation)
                .onMessage(FulFillReservationCommand.class, this::fulFillReservation)
                .build();
    }

    private Message fulFillReservation(CommandMessage<FulFillReservationCommand> cm) {
        try {
            voucherService.fulFillReservation(cm.getCommand().getSerialNumber());
            return withSuccess();
        } catch (VoucherValidationException | UnsupportedStateTransitionException e) {
            log.error("VoucherValidationException|UnsupportedStateTransitionException initiate rollback {}", e.getMessage());
            return withFailure();
        }
    }

    private Message reserveVoucher(CommandMessage<ReserveVoucherCommand> cm) {
        try {
            voucherService.reserveVoucher(cm.getCommand().getSerialNumber(), cm.getCommand().getOrderId());
            return withSuccess();
        } catch (VoucherValidationException | UnsupportedStateTransitionException e) {
            log.error("VoucherValidationException|UnsupportedStateTransitionException initiate rollback {}", e.getMessage());
            return withFailure();
        }
    }

    private Message validateVoucher(CommandMessage<ValidateVoucherCommand> cm) {
        try {
            voucherService
                    .validateVoucher(cm.getCommand().getSerialNumber(), cm.getCommand().getOrderId());
            return withSuccess();
        } catch (NoSuchVoucherException e) {
            log.error("NoSuchVoucherException {}", e.getMessage());
            return withFailure(new ValidateVoucherFailureReply(RejectionReason.NO_SUCH_VOUCHER));
        } catch (VoucherAlreadyUsedException e) {
            log.error("VoucherAlreadyUsedException {}", e.getMessage());
            return withFailure(new ValidateVoucherFailureReply(RejectionReason.VOUCHER_ALREADY_USED));
        } catch (VoucherExpiredException e) {
            log.error("VoucherExpiredException {}", e.getMessage());
            return withFailure(new ValidateVoucherFailureReply(RejectionReason.VOUCHER_EXPIRED));
        } catch (CampaignNotActiveException e) {
            log.error("CampaignNotActiveException {}", e.getMessage());
            return withFailure(new ValidateVoucherFailureReply(RejectionReason.CAMPAIGN_NOT_ACTIVE));
        }
    }

    public Message rejectReservation(CommandMessage<RejectReservationCommand> cm) {
        try {
            voucherService.rejectReservation(cm.getCommand().getSerialNumber());
            return withSuccess();
        } catch (VoucherValidationException | UnsupportedStateTransitionException e) {
            log.error("VoucherValidationException|UnsupportedStateTransitionException {}", e.getMessage());
            return withFailure();
        }
    }
}
