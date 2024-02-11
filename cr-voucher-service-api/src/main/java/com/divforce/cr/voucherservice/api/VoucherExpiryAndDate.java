package com.divforce.cr.voucherservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Oscar Makala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherExpiryAndDate {
    private String code;
    private Date expiry;
}
