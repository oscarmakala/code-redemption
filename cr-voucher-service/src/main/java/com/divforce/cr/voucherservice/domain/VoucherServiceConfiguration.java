package com.divforce.cr.voucherservice.domain;

import io.eventuate.tram.spring.flyway.EventuateTramFlywayMigrationConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Oscar Makala
 */
@Configuration
@EntityScan
@EnableJpaRepositories
@Import(EventuateTramFlywayMigrationConfiguration.class)
public class VoucherServiceConfiguration {
}
