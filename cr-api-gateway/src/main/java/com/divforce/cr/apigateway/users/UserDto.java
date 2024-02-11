package com.divforce.cr.apigateway.users;

import lombok.Data;

/**
 * @author Oscar Makala
 */
@Data
public class UserDto {
    private Boolean active;
    private String id;
    private String userName;
}
