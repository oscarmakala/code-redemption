package com.divforce.cr.voucherservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Oscar Makala
 */
public interface VoucherRepository extends JpaRepository<Voucher, String> {


    @Query("select v from Voucher v where v.campaign.id = ?1 and v.code = ?2")
    Optional<Voucher> findByCampaignIdAndCode(Long campaignId, String code);

    Page<Voucher> findBySerialNumber(String code, Pageable pageable);

    Page<Voucher> findByCampaign_Id(Long id, Pageable pageable);

}
