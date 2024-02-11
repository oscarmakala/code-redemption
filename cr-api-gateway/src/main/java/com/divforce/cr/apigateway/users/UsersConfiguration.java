package com.divforce.cr.apigateway.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Oscar Makala
 */
@Configuration
public class UsersConfiguration {

    @Bean
    public RouterFunction<ServerResponse> orderHandlerRouting(UserHandlers userHandlers) {
        return RouterFunctions
                .route()
                .GET("/users/groups/**", userHandlers::getGroups)
                .GET("/users/**", userHandlers::getUsers)
                .POST("/users/**", userHandlers::postUser)
                .build();
    }


    @Bean
    public UserHandlers userHandlers(WebClient webClient) {
        return new UserHandlers(webClient);
    }

    @Bean
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations, ServerOAuth2AuthorizedClientRepository authorizedClients) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrations,
                authorizedClients);
        oauth.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder()
                .filter(oauth)
                .build();
    }

}
