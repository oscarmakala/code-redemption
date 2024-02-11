package com.divforce.cr.audit.repository.jpa;

import com.divforce.cr.audit.repository.AuditRecordRepositoryCustom;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Default implementation of {@link AuditRecordRepositoryCustom}.
 *
 * @author Oscar Makala
 */
public class AuditRecordRepositoryImpl implements AuditRecordRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
}
