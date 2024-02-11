package com.divforce.cr.apigateway.users;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
public class UserGroup {
    private String type;
    private String origin;
    private String value;

    public UserGroup(String value) {
        this.value = value;
        this.origin = "uaa";
        this.type = "USER";
    }

}
