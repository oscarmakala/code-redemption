package com.divforce.cr.accountingservice.domain;

import com.divforce.cr.accountingservice.api.events.PaymentDomainEvent;
import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author Oscar Makala
 */
@Service
public class AccountDomainEventPublisher extends AbstractAggregateDomainEventPublisher<Payment, PaymentDomainEvent> {
    protected AccountDomainEventPublisher(DomainEventPublisher eventPublisher) {
        super(eventPublisher, Payment.class, Payment::getTransactionId);
    }
}
