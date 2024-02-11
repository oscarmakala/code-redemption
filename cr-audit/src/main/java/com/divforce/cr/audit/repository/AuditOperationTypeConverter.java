package com.divforce.cr.audit.repository;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA 2.1 {@link AttributeConverter} for the {@link AuditOperationType} enumeration.
 *
 * @author Oscar Makala
 */
@Converter
public class AuditOperationTypeConverter implements AttributeConverter<AuditOperationType, Long> {

    @Override
    public Long convertToDatabaseColumn(AuditOperationType value) {
        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public AuditOperationType convertToEntityAttribute(Long value) {
        if (value == null) {
            return null;
        }

        return AuditOperationType.fromId(value);
    }
}
