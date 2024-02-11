package com.divforce.cr.apigateway.users;

import lombok.Data;

/**
 * @author Oscar Makala
 */
@Data
public class UserName {
    private String formatted;
    private String familyName;
    private String givenName;
}
