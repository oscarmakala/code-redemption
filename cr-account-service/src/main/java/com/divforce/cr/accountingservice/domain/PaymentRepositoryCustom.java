package com.divforce.cr.accountingservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

/**
 * @author Oscar Makala
 */
public interface PaymentRepositoryCustom {
    Page<Payment> findByMobileAndDate(String mobile, Instant fromDate, Instant toDate, Pageable pageable);
}
