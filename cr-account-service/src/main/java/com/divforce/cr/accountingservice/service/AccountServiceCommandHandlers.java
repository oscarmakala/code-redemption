package com.divforce.cr.accountingservice.service;

import com.divforce.cr.accountingservice.api.AccountServiceChannels;
import com.divforce.cr.accountingservice.api.CreatePaymentCommand;
import com.divforce.cr.accountingservice.api.PaymentCommand;
import com.divforce.cr.accountingservice.api.RejectPaymentCommand;
import com.divforce.cr.accountingservice.api.events.PaymentFailure;
import com.divforce.cr.accountingservice.domain.PaymentFailedException;
import com.divforce.cr.accountingservice.domain.PaymentService;
import com.divforce.cr.common.UnsupportedStateTransitionException;
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
public class AccountServiceCommandHandlers {
    private final PaymentService accountService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(AccountServiceChannels.COMMAND_CHANNEL)
                .onMessage(CreatePaymentCommand.class, this::createPayment)
                .onMessage(PaymentCommand.class, this::payment)
                .onMessage(RejectPaymentCommand.class, this::rejectPayment)
                .build();
    }


    private Message createPayment(CommandMessage<CreatePaymentCommand> cm) {
        CreatePaymentCommand c = cm.getCommand();
        this.accountService.createPayment(
                c.getSerialNumber(),
                c.getTransactionId(),
                c.getMobile(),
                c.getAmount(),
                c.getCode(),
                c.getPassPhrase()
        );
        return withSuccess();
    }

    private Message rejectPayment(CommandMessage<RejectPaymentCommand> cm) {
        log.info("rejectPayment({})", cm.getCommand());
        RejectPaymentCommand command = cm.getCommand();
        String transactionId = command.getTransactionId();
        String thirdPartyTxnId = command.getThirdPartyTransactionId();
        String externalDataOne = command.getExternalDataOne();
        String thirdPartyStatus = command.getThirdPartyStatus();
        String errorCode = command.getErrorCode();
        String errorMessage = command.getErrorMessage();
        try {
            accountService.rejectPayment(
                    transactionId,
                    thirdPartyTxnId,
                    externalDataOne,
                    thirdPartyStatus,
                    errorCode,
                    errorMessage);
            return withSuccess();
        } catch (UnsupportedStateTransitionException e) {
            log.error("PaymentFailedException {}", e.getMessage());
            return withFailure(new PaymentFailure(
                            thirdPartyTxnId,
                            externalDataOne,
                            thirdPartyStatus,
                            errorCode,
                            errorMessage
                    )
            );
        }
    }

    private Message payment(CommandMessage<PaymentCommand> cm) {
        log.info("payment({})", cm.getCommand());
        PaymentCommand pc = cm.getCommand();
        try {
            this.accountService.makePayment(pc.getTransactionId());
            return withSuccess();
        } catch (PaymentFailedException e) {
            log.error("PaymentFailedException {}", e.getMessage());
            return withFailure(new PaymentFailure(
                            e.getThirdPartyTransactionId(),
                            e.getExternalDataOne(),
                            e.getThirdPartyStatus(),
                            e.getErrorCode(),
                            e.getErrorMessage()
                    )
            );
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            return withFailure();
        }
    }

}
