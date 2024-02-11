package com.divforce.cr.apigateway.security;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oscar Makala
 */
@Data
public class AuthorizationProperties {
    private String externalAuthoritiesUrl;
    private List<String> rules = new ArrayList<>();
    private String dashboardUrl = "/dashboard";
    private String loginUrl = "/#/login";
    private String loginProcessingUrl = "/login";
    private String logoutUrl = "/logout";
    private String logoutSuccessUrl = "/logout-success.html";
    private List<String> permitAllPaths = new ArrayList<>();
    private List<String> authenticatedPaths = new ArrayList<>();
}
