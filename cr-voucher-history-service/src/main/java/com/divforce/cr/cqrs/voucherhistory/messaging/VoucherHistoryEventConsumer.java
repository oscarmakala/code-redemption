package com.divforce.cr.cqrs.voucherhistory.messaging;

import com.divforce.cr.accountingservice.api.AccountServiceChannels;
import com.divforce.cr.accountingservice.api.events.PaymentCancelledEvent;
import com.divforce.cr.accountingservice.api.events.PaymentDisbursedEvent;
import com.divforce.cr.cqrs.voucherhistory.api.VoucherHistory;
import com.divforce.cr.cqrs.voucherhistory.domain.VoucherHistoryService;
import com.divforce.cr.voucherservice.api.VoucherServiceChannels;
import com.divforce.cr.voucherservice.api.events.*;
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
public class VoucherHistoryEventConsumer {
    private final VoucherHistoryService orderHistoryService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType(VoucherServiceChannels.EVENT_CHANNEL)
                .onEvent(VoucherCreatedEvent.class, this::handleVoucherCreated)
                .onEvent(VoucherPendingEvent.class, this::handleVoucherPending)
                .onEvent(VoucherAvailableEvent.class, this::handleVoucherAvailable)
                .onEvent(VoucherUsedEvent.class, this::handleVoucherUsed)
                .andForAggregateType(AccountServiceChannels.EVENT_CHANNEL)
                .onEvent(PaymentDisbursedEvent.class, this::handlePaymentDisbursed)
                .onEvent(PaymentCancelledEvent.class, this::handlePaymentCancelledEvent)
                .build();
    }


    private void handlePaymentCancelledEvent(DomainEventEnvelope<PaymentCancelledEvent> dee) {
        log.debug("handlePaymentCancelledEvent({})", dee);

    }

    private VoucherHistory makeVoucherHistory(String code, VoucherCreatedEvent event) {
        VoucherHistory voucherHistory = new VoucherHistory();
        voucherHistory.setId(event.getSerialNumber());
        voucherHistory.setCode(code);
        voucherHistory.setCurrency(event.getCurrency());
        voucherHistory.setExpireAt(event.getExpireAt());
        voucherHistory.setCampaignId(event.getCampaignId());
        voucherHistory.setAmount(event.getAmount().doubleValue());
        voucherHistory.setVoucherState(VoucherState.AVAILABLE);
        voucherHistory.setKeyPhrase(event.getKeyPhrase());
        return voucherHistory;
    }

    private void handlePaymentDisbursed(DomainEventEnvelope<PaymentDisbursedEvent> dee) {
        log.debug("handlePaymentDisbursed({})", dee);
        PaymentDisbursedEvent event = dee.getEvent();
        this.orderHistoryService.updatePaymentState(
                dee.getAggregateId(),
                event.getCode(),
                event.getMobile(),
                event.getThirdPartyStatus(),
                event.getExternalTransactionId(),
                event.getCompletedAt()
        );
    }

    private void handleVoucherAvailable(DomainEventEnvelope<VoucherAvailableEvent> dee) {
        log.debug("handleVoucherAvailable({})", dee);
        this.orderHistoryService.updateVoucherState(dee.getEvent().getSerialNumber(), VoucherState.AVAILABLE);
    }


    private void handleVoucherUsed(DomainEventEnvelope<VoucherUsedEvent> dee) {
        log.debug("handleVoucherUsed({})", dee);
        this.orderHistoryService.updateVoucherState(dee.getEvent().getSerialNumber(), VoucherState.USED);
    }

    private void handleVoucherPending(DomainEventEnvelope<VoucherPendingEvent> dee) {
        log.debug("handleVoucherPending({})", dee);
        this.orderHistoryService.updateVoucherState(dee.getEvent().getSerialNumber(), VoucherState.PENDING);
    }

    private void handleVoucherCreated(DomainEventEnvelope<VoucherCreatedEvent> dee) {
        log.debug("handleVoucherCreated({})", dee);
        this.orderHistoryService.addVoucherHistory(makeVoucherHistory(dee.getAggregateId(), dee.getEvent()));
    }
}
