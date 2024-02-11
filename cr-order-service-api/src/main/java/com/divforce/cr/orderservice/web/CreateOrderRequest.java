package com.divforce.cr.orderservice.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @JsonProperty("type")
    private String requestType;
    @NotBlank(message = "trxId is mandatory")
    @JsonProperty("trxId")
    private String transactionId;
    @NotBlank(message = "voucher code is mandatory")
    private String code;
    @NotBlank(message = "msisdn is mandatory")
    private String msisdn;
}
