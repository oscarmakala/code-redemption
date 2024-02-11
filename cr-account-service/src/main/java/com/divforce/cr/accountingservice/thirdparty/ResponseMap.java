package com.divforce.cr.accountingservice.thirdparty;

import lombok.Data;

/**
 * @author Oscar Makala
 */
@Data
public class ResponseMap {
    private  String externalTxnOne;
    private  String externalTxnTwo;
    private  String transactionStatus;
    private  String message;
}
