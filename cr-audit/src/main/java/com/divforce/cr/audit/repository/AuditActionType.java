package com.divforce.cr.audit.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * Represent the various actions possible for Auditing events.
 *
 * @author Oscar Makala
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuditActionType {
    CREATE(100L, "Create", "Create an Entity"),
    DELETE(200L, "Delete", "Delete an Entity"),
    DEACTIVE(300L, "DEACTIVE", "DEACTIVE an Entity"),
    UPDATE(600L, "Update", "Update an Entity"),
    LOGIN_SUCCESS(700L, "SuccessfulLogin", "Successful login");


    private Long id;

    private String name;

    private String description;

    /**
     * Constructor.
     */
    AuditActionType(final Long id, final String name, final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static AuditActionType fromId(Long auditActionTypeId) {
        Assert.notNull(auditActionTypeId, "Parameter auditActionTypeId, must not be null.");
        for (AuditActionType auditActionType : AuditActionType.values()) {
            if (auditActionType.getId().equals(auditActionTypeId)) {
                return auditActionType;
            }
        }

        return null;
    }

    public String getNameWithDescription() {
        return name + " (" + description + ")";
    }
}
