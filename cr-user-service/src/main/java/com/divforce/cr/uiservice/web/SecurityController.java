package com.divforce.cr.uiservice.web;

import com.divforce.cr.uiservice.model.SecurityInfoResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

/**
 * @author Oscar Makala
 */
@Log4j2
@RestController
@RequestMapping("/security/info")
@RequiredArgsConstructor
public class SecurityController {


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SecurityInfoResource getSecurityInfo(Principal principal) {
        log.info("getSecurityInfo({})", principal);
        final SecurityInfoResource securityInfo = new SecurityInfoResource();
        securityInfo.setAuthenticationEnabled(true);
        if (principal != null) {
            securityInfo.setAuthenticated(true);
            if (principal instanceof JwtAuthenticationToken) {
                JwtAuthenticationToken jwToken = (JwtAuthenticationToken) principal;
                Jwt token = jwToken.getToken();
                securityInfo.setUsername(token.getClaimAsString("preferred_username"));
                securityInfo.setEmail(token.getClaimAsString("email"));
                securityInfo.setRoles(((JwtAuthenticationToken) principal)
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
            }
        }
        return securityInfo;
    }


}
