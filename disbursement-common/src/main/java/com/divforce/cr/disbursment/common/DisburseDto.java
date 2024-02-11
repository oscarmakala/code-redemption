package com.divforce.cr.disbursment.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisburseDto {
    private String status;
    private String externalTxnOne;
    private String externalTxnTwo;
    private String errorCode;
    private String errorMessage;
    private String message;
}
