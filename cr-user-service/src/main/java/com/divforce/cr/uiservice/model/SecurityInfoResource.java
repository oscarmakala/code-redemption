package com.divforce.cr.uiservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oscar Makala
 */
@Data
public class SecurityInfoResource  {
    private boolean authenticationEnabled;

    private boolean authenticated;

    private String username;

    private List<String> roles = new ArrayList<>(0);
    private String email;
    private String name;

    /**
     * @param role Adds the role to {@link #roles}
     * @return the resource with an additional role
     */
    public SecurityInfoResource addRole(String role) {
        this.roles.add(role);
        return this;
    }

}

