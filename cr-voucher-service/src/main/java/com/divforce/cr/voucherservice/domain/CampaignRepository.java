package com.divforce.cr.voucherservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oscar Makala
 */
public interface CampaignRepository extends JpaRepository<Campaign, Long>, CampaignRepositoryCustom {
}
