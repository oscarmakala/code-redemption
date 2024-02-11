package com.divforce.cr.cqrs.voucherhistory.api;

import com.divforce.cr.voucherservice.api.events.VoucherState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Oscar Makala
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherHistory {
    @Id
    private String id;
    private String currency;
    private String code;
    private Date expireAt;
    private Long campaignId;
    private String transactionId;
    private VoucherState voucherState;
    private String externalTransactionId;
    private String thirdPartyStatus;
    private Double amount;
    private String mobile;
    private Date completedAt;
    private String keyPhrase;
}
