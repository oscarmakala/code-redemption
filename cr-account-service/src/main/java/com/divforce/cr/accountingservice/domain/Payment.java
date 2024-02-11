package com.divforce.cr.accountingservice.domain;

import com.divforce.cr.accountingservice.api.events.PaymentCancelledEvent;
import com.divforce.cr.accountingservice.api.events.PaymentDisbursedEvent;
import com.divforce.cr.accountingservice.api.events.PaymentDomainEvent;
import com.divforce.cr.common.UnsupportedStateTransitionException;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static com.divforce.cr.accountingservice.domain.PaymentState.PENDING;
import static com.divforce.cr.accountingservice.domain.PaymentState.SUCCESS;
import static java.util.Collections.singletonList;

/**
 * @author Oscar Makala
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "py_mobile", columnList = "mobile"),
        @Index(name = "py_created_at", columnList = "createdAt"),
        @Index(name = "py_serial_number", columnList = "serial_number"),
})
public class Payment {

    @Id
    @Column(length = 100)
    private String transactionId;
    @Column(length = 100)
    private String thirdPartyTransactionId;
    private String externalDataOne;
    @Column(length = 100)
    private String thirdPartyStatus;
    @Column(length = 20)
    private String mobile;
    private BigDecimal amount;
    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private PaymentState state;

    private Instant createdAt;
    private Instant completedAt;
    @Column(length = 400)
    @Lob
    private String message;
    @Column(name = "serial_number", length = 100)
    private String serialNumber;
    @Column(length = 40)
    private String errorCode;
    private String errorMessage;
    private String code;
    private String passPhrase;

    public Payment(String transactionId,
                   String serialNumber,
                   String mobile,
                   BigDecimal amount,
                   String code,
                   String passPhrase) {
        this.transactionId = transactionId;
        this.state = PENDING;
        this.amount = amount;
        this.mobile = mobile;
        this.serialNumber = serialNumber;
        this.createdAt = Instant.now();
        this.code = code;
        this.passPhrase = passPhrase;

    }

    public static ResultWithDomainEvents<Payment, PaymentDomainEvent> create(
            String transactionId,
            String serialNumber,
            String mobile,
            BigDecimal amount,
            String code,
            String passPhrase) {
        return new ResultWithDomainEvents<>(
                new Payment(transactionId, serialNumber, mobile, amount, code, passPhrase)
        );
    }


    public List<PaymentDomainEvent>
    disburse(String externalTxnOne, String externalTxnTwo, String transactionStatus, String msg) {
        if (state == PENDING) {
            this.state = SUCCESS;
            this.thirdPartyTransactionId = externalTxnOne;
            this.externalDataOne = externalTxnTwo;
            this.thirdPartyStatus = transactionStatus;
            this.message = msg;
            this.completedAt = Instant.now();
            return singletonList(new PaymentDisbursedEvent(this.mobile,
                    externalTxnOne,
                    transactionStatus,
                    this.serialNumber,
                    Date.from(this.completedAt))
            );
        }
        throw new UnsupportedStateTransitionException(state);
    }

    public List<PaymentDomainEvent>
    reject(String thirdPartyTxnId, String externalDataOne, String thirdPartyStatus, String errorCode, String errorMessage) {
        if (state == PENDING) {
            this.state = PaymentState.FAILED;
            this.thirdPartyTransactionId = thirdPartyTxnId;
            this.externalDataOne = externalDataOne;
            this.thirdPartyStatus = thirdPartyStatus;
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            return singletonList(new PaymentCancelledEvent());
        }
        throw new UnsupportedStateTransitionException(state);
    }
}
