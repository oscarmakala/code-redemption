package com.divforce.cr.audit.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * The application area an {@link AuditRecord} is associated with.
 *
 * @author Oscar Makala
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuditOperationType {
    VOUCHER(300L, "Voucher"),
    CAMPAIGN(400L, "Campaign"),
    LOGIN(500L, "Login");
    private final Long id;
    private final String name;

    /**
     * Constructor.
     */
    AuditOperationType(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static AuditOperationType fromId(Long auditOperationTypeId) {
        Assert.notNull(auditOperationTypeId, "Parameter auditOperationTypeId, must not be null.");
        for (AuditOperationType auditOperationType : AuditOperationType.values()) {
            if (auditOperationType.getId().equals(auditOperationTypeId)) {
                return auditOperationType;
            }
        }

        return null;
    }
}
