package com.divforce.cr.voucherservice.domain;

import com.divforce.cr.common.UnsupportedStateTransitionException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oscar Makala
 */

@Log4j2
@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name = "campaigns")
@Access(AccessType.FIELD)
@EntityListeners(AuditingEntityListener.class)
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @Column(name = "max_redemptions_per_user")
    private Integer maxRedemptionsPerUser = 0;
    @Enumerated(EnumType.STRING)
    private CampaignStatus status;
    @Column(nullable = false)
    private String passPhrase;
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voucher> vouchers = new ArrayList<>();
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;
    private String approvedBy;
    @CreatedDate
    @Column(name = "created_on")
    private Instant createdOn;

    public Campaign() {
        this.status = CampaignStatus.DRAFT;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maximumRedemptionsPerUser=" + maxRedemptionsPerUser +
                ", status=" + status +
                ", passPhrase='" + passPhrase + '\'' +
                ", vouchers=" + vouchers +
                ", createdBy='" + createdBy + '\'' +
                ", approvedBy='" + approvedBy + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }

    public void activate() {
        if (this.status != CampaignStatus.DRAFT) {
            throw new UnsupportedStateTransitionException(this.status);
        }
        this.status = CampaignStatus.ACTIVE;
    }

    /**
     * I can only deactivate a campaign if it's in DRAFT, ACTIVE, PAUSED
     */
    public void deActivate() {
        if (this.status == CampaignStatus.DEACTIVATED) {
            throw new UnsupportedStateTransitionException(this.status);
        }
        this.status = CampaignStatus.DEACTIVATE_PENDING;
    }

    public void approve(String approvedBy) {
        if (this.status != CampaignStatus.DRAFT) {
            throw new UnsupportedStateTransitionException(this.status);
        }
        this.approvedBy = approvedBy;
        this.status = CampaignStatus.ACTIVE;
    }

    public void pause() {
        if (this.status == CampaignStatus.PAUSED) {
            throw new UnsupportedStateTransitionException(this.status);
        }
        this.status = CampaignStatus.PAUSED_PENDING;
    }

    public void reject(String approvedBy) {
        if (this.status != CampaignStatus.DRAFT) {
            throw new UnsupportedStateTransitionException(this.status);
        }
        this.approvedBy = approvedBy;
        this.status = CampaignStatus.REJECTED;
    }

    public boolean hasVouchers() {
        return !this.vouchers.isEmpty();
    }
}
