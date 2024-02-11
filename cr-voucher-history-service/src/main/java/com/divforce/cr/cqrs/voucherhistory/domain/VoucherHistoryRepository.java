package com.divforce.cr.cqrs.voucherhistory.domain;

import com.divforce.cr.cqrs.voucherhistory.api.VoucherHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Oscar Makala
 */
public interface VoucherHistoryRepository extends MongoRepository<VoucherHistory, String> {
    Page<VoucherHistory> findByCampaignId(Long campaignId, Pageable pageable);


    Optional<VoucherHistory> findByTransactionId(String transactionId);

    Page<VoucherHistory> findByCampaignIdAndId(Long campaignId, String serial, Pageable pageable);


    @Aggregation("{ $group :{ _id: $voucherState, totalAmount: { $sum: $amount }, totalCount: { $sum: 1 } } }")
    List<VoucherHistoryAggregate> groupByVoucherState();

}
