package com.divforce.cr.orderservice.service;

import com.divforce.cr.common.RejectionReason;
import com.divforce.cr.orderservice.OrderServiceChannels;
import com.divforce.cr.orderservice.domain.OrderService;
import com.divforce.cr.orderservice.sagaparticipants.RejectOrderCommand;
import com.divforce.cr.orderservice.sagaparticipants.SubmitOrderCommand;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class OrderCommandHandlers {
    private final OrderService orderService;

    public CommandHandlers commandHandlers() {
        return SagaCommandHandlersBuilder
                .fromChannel(OrderServiceChannels.COMMAND_CHANNEL)
                .onMessage(SubmitOrderCommand.class, this::submitOrder)
                .onMessage(RejectOrderCommand.class, this::rejectOrder)
                .build();
    }

    private Message submitOrder(CommandMessage<SubmitOrderCommand> cm) {
        log.info("rejectOrder({})",cm.getCommand());
        String orderId = cm.getCommand().getOrderId();
        orderService.submitOrder(orderId);
        return withSuccess();
    }

    private Message rejectOrder(CommandMessage<RejectOrderCommand> cm) {
        RejectOrderCommand command = cm.getCommand();
        log.info("rejectOrder({})",command);
        String orderId = command.getOrderId();
        RejectionReason rejectionReason = command.getRejectionReason();
        orderService.rejectOrder(orderId, rejectionReason);
        return withSuccess();
    }
}
