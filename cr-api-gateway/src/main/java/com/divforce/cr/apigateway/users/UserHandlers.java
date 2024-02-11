package com.divforce.cr.apigateway.users;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author Oscar Makala
 */

public class UserHandlers {
    private final WebClient webClient;

    public UserHandlers(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ServerResponse> getUsers(ServerRequest serverRequest) {
        return this.webClient
                .get()
                .uri("http://uaa:8080/Users")
                .retrieve()
                .bodyToMono(UserDtoPage.class)
                .flatMap(od -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(od))
                );
    }

    public Mono<ServerResponse> postUser(ServerRequest request) {
        String groupId = request.queryParam("groupId").orElse("");
        return request.bodyToMono(CreateUser.class)
                .flatMap(createUser -> this.webClient
                        .post()
                        .uri("http://uaa:8080/Users")
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(createUser)
                        .retrieve()
                        .bodyToMono(UserDto.class)
                        .flatMap(userDto -> this.webClient.post()
                                .uri("http://uaa:8080/Groups/" + groupId + "/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserGroup(userDto.getId()))
                                .retrieve()
                                .bodyToMono(UserGroup.class)
                        ).flatMap(od -> ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromObject(od)))
                );


    }

    public Mono<ServerResponse> getGroups(ServerRequest serverRequest) {
        return this.webClient
                .get()
                .uri("http://uaa:8080/Groups?filter=displayName eq \"role.manage\" or displayName eq \"uaa.admin\" or displayName eq \"role.view\"")
                .retrieve()
                .bodyToMono(GroupDtoPage.class)
                .flatMap(od -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(od))
                );
    }
}
