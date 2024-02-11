package com.divforce.cr.disbursment.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author Oscar Makala
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractDisbursementSource {
    private String template;
    private String transactor;
    private String pin;
    private String endpoint;
    private Map<String, String> headers;
}
