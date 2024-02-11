package com.divforce.cr.voucherservice.web;

import com.divforce.cr.encrypt.util.DecryptUtil;
import com.divforce.cr.voucherservice.api.events.VoucherState;
import com.divforce.cr.voucherservice.domain.Voucher;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

/**
 * @author Oscar Makala
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherResource extends RepresentationModel<VoucherResource> {
    private String createdBy;
    private Instant createdOn;
    private Date usedTime;
    private String state;
    private String code;
    private BigDecimal amount;
    private String currency;
    private Date expireAt;
    private String serialNumber;

    public VoucherResource(Voucher entity) {
        this.serialNumber = entity.getSerialNumber();
        this.amount = entity.getAmount();
        this.currency = entity.getCurrency();
        this.expireAt = entity.getExpireAt();
        this.state = entity.getState().toString();
        this.createdBy = entity.getCreatedBy();
        this.createdOn = entity.getCreatedOn();
        if (entity.getUsedTime() != null) {
            this.usedTime = Date.from(entity.getUsedTime().toInstant());
        }
        if (VoucherState.USED.equals(entity.getState())) {
            this.code = DecryptUtil.decryptCode(this.serialNumber, entity.getCode(), entity.getCampaign().getPassPhrase());
        } else {
            this.code = entity.getCode();
        }
    }
}
