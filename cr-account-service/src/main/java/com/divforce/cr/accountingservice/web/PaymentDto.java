package com.divforce.cr.accountingservice.web;

import com.divforce.cr.accountingservice.domain.Payment;
import com.divforce.cr.encrypt.util.AttributeEncryptor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Oscar Makala
 */
@Log4j2
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class PaymentDto extends RepresentationModel<PaymentDto> {
    private String transactionId;
    private String thirdPartyTransactionId;
    private String externalDataOne;
    private String thirdPartyStatus;
    private String mobile;
    private BigDecimal amount;
    private String state;
    private Date createdAt;
    private Date completedAt;
    private String message;
    private String serialNumber;
    private String errorCode;
    private String errorMessage;

    public PaymentDto(Payment entity) {
        this.transactionId = entity.getTransactionId();
        this.thirdPartyTransactionId = entity.getThirdPartyTransactionId();
        this.externalDataOne = entity.getExternalDataOne();
        this.thirdPartyStatus = entity.getThirdPartyStatus();
        this.mobile = entity.getMobile();
        this.amount = entity.getAmount();
        this.state = entity.getState().toString();
        this.createdAt = Date.from(entity.getCreatedAt());
        this.serialNumber = entity.getSerialNumber();
        this.errorCode = entity.getErrorCode();
        this.errorMessage = entity.getErrorMessage();
        if ("SUCCESS".equals(this.state)) {
            this.serialNumber = decryptCode(this.serialNumber, entity.getCode(), entity.getPassPhrase());
        }
    }

    private String decryptCode(String serialNumber, String code, String passPhrase) {
        try {
            AttributeEncryptor attributeEncryptor = new AttributeEncryptor();
            passPhrase = attributeEncryptor.convertToEntityAttribute(passPhrase);
            AttributeEncryptor encrypt = new AttributeEncryptor(passPhrase);
            return encrypt.convertToEntityAttribute(code);
        } catch (Exception e) {
            log.debug("decryptCode", e);
            return serialNumber;
        }
    }
}
