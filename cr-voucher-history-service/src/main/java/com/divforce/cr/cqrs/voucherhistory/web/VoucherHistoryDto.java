package com.divforce.cr.cqrs.voucherhistory.web;

import com.divforce.cr.cqrs.voucherhistory.api.VoucherHistory;
import com.divforce.cr.encrypt.util.DecryptUtil;
import com.divforce.cr.voucherservice.api.events.VoucherState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

/**
 * @author Oscar Makala
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherHistoryDto extends RepresentationModel<VoucherHistoryDto> {
    private String mobile;
    private String code;
    private Double amount;
    private String id;
    private String currency;
    private Date expireAt;
    private Long campaignId;
    private VoucherState voucherState;
    private String externalTransactionId;
    private String thirdPartyStatus;
    private String orderId;
    private Date completedAt;

    public VoucherHistoryDto(VoucherHistory entity) {
        this.id = entity.getId();
        this.code = entity.getCode();
        this.currency = entity.getCurrency();
        this.expireAt = entity.getExpireAt();
        this.voucherState = entity.getVoucherState();
        this.externalTransactionId = entity.getExternalTransactionId();
        this.thirdPartyStatus = entity.getThirdPartyStatus();
        this.campaignId = entity.getCampaignId();
        this.amount = entity.getAmount();
        this.orderId = entity.getTransactionId();
        this.mobile = entity.getMobile();
        this.completedAt = entity.getCompletedAt();
        if (VoucherState.USED.equals(this.voucherState)) {
            this.id = DecryptUtil.decryptCode(this.id, this.code, entity.getKeyPhrase());
        }
    }


}
