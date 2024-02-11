package com.divforce.cr.uiservice.config;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Oscar Makala
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String ROLES = "roles";
    private static final String RESOURCE_ACCESS = "resource_access";
    private final String oidcClient;
    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final Gson gson;

    public KeycloakJwtAuthenticationConverter(String oidcClient) {
        this.oidcClient = oidcClient;
        this.gson = new Gson();
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt theJwt) {
        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(theJwt);
        JsonObject keycloakClientAuthorities = gson.toJsonTree(theJwt.getClaimAsMap(RESOURCE_ACCESS).get(oidcClient)).getAsJsonObject();
        if (keycloakClientAuthorities != null) {
            JsonArray clientRoles = (JsonArray) keycloakClientAuthorities.get(ROLES);
            if (clientRoles != null) {
                Collection<GrantedAuthority> clientAuthorities = clientRoles.asList().stream()
                        .map(JsonElement::getAsString)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                return new JwtAuthenticationToken(theJwt, clientAuthorities);
            }
        }
        return new JwtAuthenticationToken(theJwt, authorities);
    }
}
