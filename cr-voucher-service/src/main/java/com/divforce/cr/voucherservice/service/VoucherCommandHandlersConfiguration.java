package com.divforce.cr.voucherservice.service;

import com.divforce.cr.encrypt.util.AttributeEncryptor;
import com.divforce.cr.security.common.SpringSecurityAuditorAware;
import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Oscar Makala
 */
@EnableJpaAuditing
@Configuration
@Import({
        SagaParticipantConfiguration.class,
        TramEventsPublisherConfiguration.class
})
public class VoucherCommandHandlersConfiguration {
    @Bean
    public CommandDispatcher commandDispatcher(VoucherServiceCommandHandlers voucherServiceCommandHandlers,
                                               SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory
                .make("voucherServiceDispatcher", voucherServiceCommandHandlers.commandHandlers());
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }

    @Bean
    AttributeEncryptor attributeEncryptor() throws Exception {
        return new AttributeEncryptor();
    }
}
