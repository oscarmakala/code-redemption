package com.divforce.cr.accountingservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oscar Makala
 */
public interface PaymentRepository extends JpaRepository<Payment, String>,PaymentRepositoryCustom {
    Page<Payment> findAllByMobileContainingIgnoreCase(String mobile, Pageable pageable);


}
