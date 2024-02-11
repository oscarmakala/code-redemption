package com.divforce.cr.notificationservice.messaging;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Oscar Makala
 */
@Configuration
@Import({TramEventSubscriberConfiguration.class})
public class NotificationServiceMessagingConfiguration {

    @Bean
    public DomainEventDispatcher domainEventDispatcher(NotificationEventConsumer notificationEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("notificationServiceEvents", notificationEventConsumer.domainEventHandlers());
    }
}
