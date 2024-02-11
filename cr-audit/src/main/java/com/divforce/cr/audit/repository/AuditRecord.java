package com.divforce.cr.audit.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * @author Oscar Makala
 */
@ToString
@Setter
@Getter
@Entity
@Table(name = "AuditRecords")
@EntityListeners(AuditingEntityListener.class)
public class AuditRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "correlation_id")
    private String correlationId;

    @Lob
    @Column(name = "audit_data")
    private String auditData;

    @CreatedDate
    @Column(name = "created_on")
    private Instant createdOn;

    @NotNull
    @Convert(converter = AuditActionTypeConverter.class)
    @Column(name = "audit_action")
    private AuditActionType auditAction;

    @NotNull
    @Convert(converter = AuditOperationTypeConverter.class)
    @Column(name = "audit_operation")
    private AuditOperationType auditOperation;
}
