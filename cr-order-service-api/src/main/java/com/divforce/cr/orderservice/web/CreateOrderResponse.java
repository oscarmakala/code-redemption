package com.divforce.cr.orderservice.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    private String statusCode;
    private String message;
    private String amount;
}
