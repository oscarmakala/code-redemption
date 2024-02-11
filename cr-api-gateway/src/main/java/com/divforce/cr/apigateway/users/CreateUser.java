package com.divforce.cr.apigateway.users;

import lombok.Data;

import java.util.List;

/**
 * @author Oscar Makala
 */
@Data
public class CreateUser {
    private String externalId;
    private String origin;
    private String[] schemas;
    private String userName;
    private String password;
    private UserName name;
    private List<UserEmail> emails;
    private Boolean active;

}
