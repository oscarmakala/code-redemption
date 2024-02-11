package com.divforce.cr.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for managing the {@link AuditRecord} class.
 *
 * @author Oscar Makala
 */
@Transactional
@Repository
public interface AuditRecordRepository extends JpaRepository<AuditRecord, Long>, AuditRecordRepositoryCustom {
}
