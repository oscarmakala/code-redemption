package com.divforce.cr.voucherservice.security;

import com.divforce.cr.common.AuthorizationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oscar Makala
 */
@Configuration
public class SecurityBeanConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.cloud.coderedeem.security.authorization")
    public AuthorizationProperties authorizationProperties() {
        return new AuthorizationProperties();
    }


}
