package com.divforce.cr.voucherservice.domain;

import com.divforce.cr.common.UnsupportedStateTransitionException;
import com.divforce.cr.voucherservice.api.events.*;
import com.divforce.cr.voucherservice.exception.CampaignNotActiveException;
import com.divforce.cr.voucherservice.exception.VoucherAlreadyUsedException;
import com.divforce.cr.voucherservice.exception.VoucherExpiredException;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author Oscar Makala
 */
@Log4j2
@Getter
@Setter
@Entity
@Table(name = "vouchers")
@EntityListeners(AuditingEntityListener.class)
public class Voucher {
    @Id
    private String serialNumber;
    private String code;
    private BigDecimal amount;
    @Column(length = 10)
    private String currency;
    private Date expireAt;
    @Enumerated(EnumType.STRING)
    private VoucherState state;
    @ManyToOne(fetch = FetchType.EAGER)
    private Campaign campaign;
    private ZonedDateTime pendingTime;
    private ZonedDateTime usedTime;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;
    @CreatedDate
    @Column(name = "created_on")
    private Instant createdOn;

    public Voucher() {
        this.state = VoucherState.AVAILABLE;
    }

    public static ResultWithDomainEvents<Voucher, VoucherDomainEvent> create(String code,
                                                                             BigDecimal amount,
                                                                             String currency,
                                                                             Date expireAt,
                                                                             Campaign campaign,
                                                                             String serialNumber) {
        Voucher voucher = new Voucher();
        voucher.setSerialNumber(serialNumber);
        voucher.setCampaign(campaign);
        voucher.setAmount(amount);
        voucher.setCode(code);
        voucher.setExpireAt(expireAt);
        voucher.setCurrency(currency);
        voucher.setSerialNumber(serialNumber);
        List<VoucherDomainEvent> events = singletonList(new VoucherCreatedEvent(
                        serialNumber,
                        voucher.getAmount(),
                        voucher.getCurrency(),
                        voucher.getExpireAt(),
                        voucher.getCode(),
                        voucher.getCampaign().getId(),
                        campaign.getPassPhrase()
                )
        );
        return new ResultWithDomainEvents<>(voucher, events);
    }


    public List<VoucherDomainEvent> reserve() {
        if (state == VoucherState.AVAILABLE) {
            state = VoucherState.PENDING;
            this.pendingTime = ZonedDateTime.now();
            return singletonList(new VoucherPendingEvent(serialNumber));
        }
        throw new UnsupportedStateTransitionException(state);
    }

    public List<VoucherDomainEvent> rejectReservation() {
        if (state == VoucherState.PENDING) {
            state = VoucherState.AVAILABLE;
            return singletonList(new VoucherAvailableEvent(serialNumber));
        }
        throw new UnsupportedStateTransitionException(state);
    }


    public List<VoucherDomainEvent> used() {
        if (state == VoucherState.PENDING) {
            state = VoucherState.USED;
            usedTime = ZonedDateTime.now();
            return singletonList(new VoucherUsedEvent(serialNumber));
        }
        throw new UnsupportedStateTransitionException(state);
    }

    /**
     * 1. Check if the campaign is active.
     * 2. Check if the voucher is USED or PENDING
     * 3. Check if voucher has expired
     */
    public void validateVoucher() {
        if (this.campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new CampaignNotActiveException(this.serialNumber);
        }
        if (this.state == VoucherState.USED || this.state == VoucherState.PENDING) {
            throw new VoucherAlreadyUsedException(this.serialNumber);
        } else if (this.getExpireAt().before(new Date())) {
            throw new VoucherExpiredException(this.serialNumber);
        }
    }
}
