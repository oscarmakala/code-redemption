package com.divforce.cr.voucherservice.domain;

import com.divforce.cr.voucherservice.api.events.VoucherDomainEvent;
import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author Oscar Makala
 */
@Service
public class VoucherDomainEventPublisher extends AbstractAggregateDomainEventPublisher<Voucher, VoucherDomainEvent> {
    public VoucherDomainEventPublisher(DomainEventPublisher eventPublisher) {
        super(eventPublisher, Voucher.class, Voucher::getCode);
    }
}
