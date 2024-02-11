package com.divforce.cr.azampay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AzamError {
    private String code;
    private String message;
}
