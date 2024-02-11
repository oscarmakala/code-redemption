package com.divforce.cr.accountingservice.thirdparty;

import lombok.Data;

import java.util.Map;

/**
 * @author Oscar Makala
 */
@Data
public class ThirdPartyProperties {
    private String template;
    private String endpoint;
    private ResponseMap responseMap;
    private Map<String, String> headers;
}
