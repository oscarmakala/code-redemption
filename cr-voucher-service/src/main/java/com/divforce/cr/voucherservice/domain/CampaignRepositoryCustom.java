package com.divforce.cr.voucherservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface CampaignRepositoryCustom {

    Page<Campaign> findByCampaignStatus(CampaignStatus[] campaignStatus, Instant fromDate, Instant toDate, Pageable pageable);
}
