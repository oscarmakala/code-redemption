package com.divforce.cr.audit;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Oscar Makala
 */
@EntityScan({"com.divforce.cr.audit.repository"})
@EnableJpaRepositories(basePackages = {"com.divforce.cr.audit.repository"})
@ComponentScan(basePackages = {"com.divforce.cr.audit"})
@Configuration
public class AppAuditingConfiguration {


}
