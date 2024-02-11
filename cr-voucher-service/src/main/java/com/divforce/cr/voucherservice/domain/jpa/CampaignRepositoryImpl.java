package com.divforce.cr.voucherservice.domain.jpa;

import com.divforce.cr.voucherservice.domain.Campaign;
import com.divforce.cr.voucherservice.domain.CampaignRepositoryCustom;
import com.divforce.cr.voucherservice.domain.CampaignStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CampaignRepositoryImpl implements CampaignRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Campaign> findByCampaignStatus(CampaignStatus[] statuses, Instant fromDate, Instant toDate, Pageable pageable) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Campaign> query = cb.createQuery(Campaign.class);
        final Root<Campaign> campaignRoot = query.from(Campaign.class);

        final Path<CampaignStatus> campaignStatus = campaignRoot.get("status");
        final Path<Instant> createdOn = campaignRoot.get("createdOn");


        final List<Predicate> campaignStatusPredicates = new ArrayList<>();
        if (statuses != null && statuses.length > 0) {
            for (CampaignStatus status : statuses) {
                campaignStatusPredicates.add(cb.equal(campaignStatus, status));
            }
        }
        final List<Predicate> finalQueryPredicates = new ArrayList<>();

        if (!campaignStatusPredicates.isEmpty()) {
            final Predicate campaignStatusPredicatesOr = cb.or(campaignStatusPredicates.toArray(new Predicate[0]));
            finalQueryPredicates.add(campaignStatusPredicatesOr);
        }

        final CriteriaQuery<Campaign> select = query.select(campaignRoot);
        if (!finalQueryPredicates.isEmpty()) {
            select.where(finalQueryPredicates.toArray(new Predicate[0]));
        }

        if (pageable.getSort().isUnsorted()) {
            select.orderBy(QueryUtils.toOrders(pageable.getSort().and(Sort.by("id")).descending(), campaignRoot, cb));
        } else {
            select.orderBy(QueryUtils.toOrders(pageable.getSort(), campaignRoot, cb));
        }


        final TypedQuery<Campaign> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        final List<Campaign> resultList = typedQuery.getResultList();

        final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Campaign.class)));

        if (!finalQueryPredicates.isEmpty()) {
            countQuery.where(finalQueryPredicates.toArray(new Predicate[0]));
        }

        final Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, totalCount);
    }
}
