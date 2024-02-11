package com.divforce.cr.notificationservice.messaging;

import com.divforce.cr.common.RejectionReason;
import com.divforce.cr.notificationservice.domain.NotificationService;
import com.divforce.cr.orderservice.OrderServiceChannels;
import com.divforce.cr.orderservice.events.OrderProcessedEvent;
import com.divforce.cr.orderservice.events.OrderRejectedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Oscar Makala
 */
@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {
    private final NotificationService notificationService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder.forAggregateType(OrderServiceChannels.ORDER_EVENT_CHANNEL)
                .onEvent(OrderRejectedEvent.class, this::sendFailedNotification)
                .onEvent(OrderProcessedEvent.class, this::sendSuccessNotification)
                .build();
    }

    private void sendSuccessNotification(DomainEventEnvelope<OrderProcessedEvent> de) {
        OrderProcessedEvent event = de.getEvent();
        String orderId = event.getOrderId();
        String mobile = event.getMobileNumber();
        BigDecimal amount = event.getAmount();
        this.notificationService.send(orderId, mobile, RejectionReason.VOUCHER_REDEEMED, new Object[]{amount});
    }

    private void sendFailedNotification(DomainEventEnvelope<OrderRejectedEvent> de) {
        String orderId = de.getEvent().getOrderId();
        String mobile = de.getEvent().getMobileNumber();
        RejectionReason rejectionReason = de.getEvent().getRejectionReason();
        this.notificationService.send(orderId, mobile, rejectionReason, new Object[]{});
    }
}
