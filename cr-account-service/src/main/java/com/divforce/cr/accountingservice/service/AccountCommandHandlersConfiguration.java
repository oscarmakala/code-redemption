package com.divforce.cr.accountingservice.service;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.flyway.EventuateTramFlywayMigrationConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Oscar Makala
 */
@Configuration
@Import({
        OptimisticLockingDecoratorConfiguration.class,
        EventuateTramFlywayMigrationConfiguration.class,
        TramEventsPublisherConfiguration.class
})
public class AccountCommandHandlersConfiguration {
    @Bean
    public CommandDispatcher commandDispatcher(AccountServiceCommandHandlers accountServiceCommandHandlers,
                                               SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory
                .make("accountServiceDispatcher", accountServiceCommandHandlers.commandHandlers());
    }
}
