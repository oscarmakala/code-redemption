package com.divforce.cr.orderservice.messaging;

import com.divforce.cr.orderservice.domain.OrderService;
import com.divforce.cr.orderservice.domain.VoucherState;
import com.divforce.cr.voucherservice.api.VoucherServiceChannels;
import com.divforce.cr.voucherservice.api.events.VoucherAvailableEvent;
import com.divforce.cr.voucherservice.api.events.VoucherCreatedEvent;
import com.divforce.cr.voucherservice.api.events.VoucherPendingEvent;
import com.divforce.cr.voucherservice.api.events.VoucherUsedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final OrderService orderService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType(VoucherServiceChannels. EVENT_CHANNEL)
                .onEvent(VoucherCreatedEvent.class, this::makeVoucherCreated)
                .onEvent(VoucherPendingEvent.class, this::makeVoucherPending)
                .onEvent(VoucherAvailableEvent.class, this::makeVoucherAvailable)
                .onEvent(VoucherUsedEvent.class, this::makeVoucherUsed)
                .build();
    }

    private void makeVoucherCreated(DomainEventEnvelope<VoucherCreatedEvent> de) {
        log.info("makeVoucherCreated({})", de);
        VoucherCreatedEvent voucherCreatedEvent = de.getEvent();
        orderService.createVoucher(voucherCreatedEvent);
    }

    private void makeVoucherPending(DomainEventEnvelope<VoucherPendingEvent> de) {
        log.info("makeVoucherPending({})", de);
        VoucherPendingEvent event = de.getEvent();
        orderService.updateVoucher(event.getSerialNumber(), VoucherState.PENDING);
    }

    private void makeVoucherAvailable(DomainEventEnvelope<VoucherAvailableEvent> de) {
        log.info("makeVoucherAvailable({})", de);
        VoucherAvailableEvent event = de.getEvent();
        orderService.updateVoucher(event.getSerialNumber(), VoucherState.AVAILABLE);
    }

    private void makeVoucherUsed(DomainEventEnvelope<VoucherUsedEvent> de) {
        log.info("makeVoucherUsed({})", de);
        VoucherUsedEvent event = de.getEvent();
        orderService.updateVoucher(event.getSerialNumber(), VoucherState.USED);
    }


}
