package com.divforce.cr.common;

import java.util.HashMap;
import java.util.Map;

public class ProviderRoleMapping {
    private String rolePrefix = "ROLE_";
    private String groupClaim = "roles";
    private boolean mapOauthScopes = false;
    private boolean mapGroupClaims = false;
    private Map<String, String> roleMappings = new HashMap<>(0);
    private Map<String, String> groupMappings = new HashMap<>(0);
    private String principalClaimName;

    public ProviderRoleMapping() {
        super();
    }
}
