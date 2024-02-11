package com.divforce.cr.apigateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

/**
 * @author Oscar Makala
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/security/info", "/security/login", "/orders").permitAll()
                        .anyExchange()
                        .authenticated()
                )
                .oauth2Login(oAuth2LoginSpec -> {
                }).logout(logoutSpec -> logoutSpec.logoutSuccessHandler(logoutSuccessHandler()))
                .build();

    }


    @Bean
    ServerLogoutSuccessHandler logoutSuccessHandler() {
        OidcClientInitiatedServerLogoutSuccessHandler logoutHandler = new OidcClientInitiatedServerLogoutSuccessHandler(this.reactiveClientRegistrationRepository);
        logoutHandler.setPostLogoutRedirectUri("{baseUrl}");
        return logoutHandler;
    }


}
