package com.divforce.cr.audit.repository;

import jakarta.persistence.AttributeConverter;

/**
 * JPA 2.1 {@link AttributeConverter} for the {@link AuditActionType} enumeration.
 *
 * @author Oscar Makala
 */
public class AuditActionTypeConverter implements AttributeConverter<AuditActionType, Long> {
    @Override
    public Long convertToDatabaseColumn(AuditActionType value) {
        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public AuditActionType convertToEntityAttribute(Long value) {
        if (value == null) {
            return null;
        }

        return AuditActionType.fromId(value);
    }
}
