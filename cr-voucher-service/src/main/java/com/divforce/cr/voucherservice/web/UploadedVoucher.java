package com.divforce.cr.voucherservice.web;

import com.univocity.parsers.annotations.Format;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Oscar Makala
 */

//Alphanumeric code, redemption value(Amount), Currency(if blank then TZS, else as defined), Expiry date(Expiry date - if blank, then infinite, else finite.)
@Data
@ToString
public class UploadedVoucher {
    @Parsed
    @Trim
    private String code;

    @Parsed
    private BigDecimal amount;

    @Parsed(defaultNullRead = "TZS")
    @Trim
    private String currency;

    @Parsed(defaultNullRead = "9999-12-12")
    @Format(formats = {"yyyy-MM-dd", "dd/MM/yyyy"}, options = "locale=en;lenient=false")
    private Date expireAt;
}
