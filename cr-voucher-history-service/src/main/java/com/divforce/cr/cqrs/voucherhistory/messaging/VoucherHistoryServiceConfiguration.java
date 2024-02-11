package com.divforce.cr.cqrs.voucherhistory.messaging;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Oscar Makala
 */
@Configuration
@Import({TramEventSubscriberConfiguration.class, TramNoopDuplicateMessageDetectorConfiguration.class})
public class VoucherHistoryServiceConfiguration {
    @Bean
    public DomainEventDispatcher domainEventDispatcher(VoucherHistoryEventConsumer voucherHistoryEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("voucherHistoryDomainEventDispatcher", voucherHistoryEventConsumer.domainEventHandlers());
    }
}
