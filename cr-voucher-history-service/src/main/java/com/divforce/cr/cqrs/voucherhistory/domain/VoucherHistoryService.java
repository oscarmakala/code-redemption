package com.divforce.cr.cqrs.voucherhistory.domain;

import com.divforce.cr.common.SerialNumberGenerator;
import com.divforce.cr.cqrs.voucherhistory.api.VoucherHistory;
import com.divforce.cr.voucherservice.api.events.VoucherState;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author Oscar Makala
 */
@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class VoucherHistoryService {
    private final VoucherHistoryRepository voucherHistoryRepository;

    public void addVoucherHistory(VoucherHistory voucherHistory) {
        this.voucherHistoryRepository.save(voucherHistory);
    }

    public void updateVoucherState(String code, VoucherState state) {
        voucherHistoryRepository
                .findById(code)
                .ifPresent(voucherHistory -> {
                    voucherHistory.setVoucherState(state);
                    this.voucherHistoryRepository.save(voucherHistory);
                });
    }

    public void updatePaymentState(String transactionId,
                                   String code,
                                   String mobile,
                                   String thirdPartyStatus,
                                   String externalTransactionId,
                                   Date completedAt) {
        voucherHistoryRepository
                .findById(code)
                .ifPresent(voucherHistory -> {
                    voucherHistory.setExternalTransactionId(externalTransactionId);
                    voucherHistory.setThirdPartyStatus(thirdPartyStatus);
                    voucherHistory.setTransactionId(transactionId);
                    voucherHistory.setMobile(mobile);
                    voucherHistory.setCompletedAt(completedAt);
                    this.voucherHistoryRepository.save(voucherHistory);
                });
    }

    public Page<VoucherHistory> findByCampaignIdAndCode(Long campaignId, String code, Pageable pageable) {
        Page<VoucherHistory> result;
        if (StringUtils.hasText(code)) {
            //decode the text.
            String serial = SerialNumberGenerator.generate(code);
            result = this.voucherHistoryRepository.findByCampaignIdAndId(campaignId, serial, pageable);
        } else {
            result = this.voucherHistoryRepository.findByCampaignId(campaignId, pageable);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<VoucherHistoryAggregate> aggregateVouchersByState() {
        return this.voucherHistoryRepository.groupByVoucherState();
    }
}
