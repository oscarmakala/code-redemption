package com.divforce.cr.accountingservice.domain.jpa;

import com.divforce.cr.accountingservice.domain.Payment;
import com.divforce.cr.accountingservice.domain.PaymentRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oscar Makala
 */
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Payment> findByMobileAndDate(String mobile, Instant fromDate, Instant toDate, Pageable pageable) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Payment> query = cb.createQuery(Payment.class);
        final Root<Payment> paymentRoot = query.from(Payment.class);

        final Path<String> paymentMobile = paymentRoot.get("mobile");
        final Path<Instant> paymentCreatedAt = paymentRoot.get("createdAt");

        final Predicate datePredicate;
        if (fromDate != null && toDate == null) {
            datePredicate = cb.greaterThanOrEqualTo(paymentCreatedAt, fromDate);
        } else if (fromDate == null && toDate != null) {
            datePredicate = cb.lessThanOrEqualTo(paymentCreatedAt, toDate);
        } else if (fromDate != null) {
            datePredicate = cb.between(paymentCreatedAt, fromDate, toDate);
        } else {
            datePredicate = null;
        }

        final Predicate mobilePredicate;
        if (StringUtils.hasText(mobile)) {
            mobilePredicate = cb.like(paymentMobile, mobile + "%");
        } else {
            mobilePredicate = null;
        }


        final List<Predicate> finalQueryPredicates = new ArrayList<>();


        if (datePredicate != null) {
            finalQueryPredicates.add(datePredicate);
        }

        if (mobilePredicate != null) {
            finalQueryPredicates.add(mobilePredicate);
        }

        final CriteriaQuery<Payment> select = query.select(paymentRoot);

        if (!finalQueryPredicates.isEmpty()) {
            select.where(finalQueryPredicates.toArray(new Predicate[0]));
        }

        if (pageable.getSort().isUnsorted()) {
            select.orderBy(QueryUtils.toOrders(pageable.getSort().and(Sort.by("createdAt")).ascending(), paymentRoot, cb));
        } else {
            select.orderBy(QueryUtils.toOrders(pageable.getSort(), paymentRoot, cb));
        }

        final TypedQuery<Payment> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());


        final List<Payment> resultList = typedQuery.getResultList();

        final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Payment.class)));

        if (!finalQueryPredicates.isEmpty()) {
            countQuery.where(finalQueryPredicates.toArray(new Predicate[0]));
        }

        final Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, totalCount);
    }
}
