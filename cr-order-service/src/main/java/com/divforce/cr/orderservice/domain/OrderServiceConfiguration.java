package com.divforce.cr.orderservice.domain;

import com.divforce.cr.orderservice.sagaparticipants.AccountServiceProxy;
import com.divforce.cr.orderservice.sagaparticipants.OrderServiceProxy;
import com.divforce.cr.orderservice.sagaparticipants.VoucherServiceProxy;
import com.divforce.cr.orderservice.sagas.createorder.CreateOrderSaga;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.flyway.EventuateTramFlywayMigrationConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Oscar Makala
 */
@Configuration
@Import({OptimisticLockingDecoratorConfiguration.class, EventuateTramFlywayMigrationConfiguration.class})
public class OrderServiceConfiguration {

    @Bean
    public CreateOrderSaga createOrderSaga(OrderServiceProxy orderService, VoucherServiceProxy voucherService, AccountServiceProxy accountServiceProxy) {
        return new CreateOrderSaga(orderService, voucherService, accountServiceProxy);
    }


}
