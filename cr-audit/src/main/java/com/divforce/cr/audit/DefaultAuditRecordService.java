package com.divforce.cr.audit;

import com.divforce.cr.audit.repository.AuditActionType;
import com.divforce.cr.audit.repository.AuditOperationType;
import com.divforce.cr.audit.repository.AuditRecord;
import com.divforce.cr.audit.repository.AuditRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Default implementation of the {@link AuditRecordService}.
 *
 * @author Oscar Makala
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DefaultAuditRecordService implements AuditRecordService {
    private final AuditRecordRepository auditRecordRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public AuditRecord populateAndSaveAuditRecordUsingMapData(AuditOperationType auditOperationType, AuditActionType auditActionType, Map<String, Object> data) {
        String dataAsString;
        try {
            dataAsString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Error serializing audit record data.  Data = " + data);
            dataAsString = "Error serializing audit record data.  Data = " + data;
        }
        return this.populateAndSaveAuditRecord(auditOperationType, auditActionType, dataAsString);
    }

    @Override
    public AuditRecord populateAndSaveAuditRecord(AuditOperationType auditOperationType, AuditActionType auditActionType, String data) {
        Assert.notNull(auditActionType, "auditActionType must not be null.");
        Assert.notNull(auditOperationType, "auditOperationType must not be null.");
        final AuditRecord auditRecord = new AuditRecord();
        auditRecord.setAuditAction(auditActionType);
        auditRecord.setAuditOperation(auditOperationType);
        auditRecord.setAuditData(data);
        return this.auditRecordRepository.save(auditRecord);
    }
}
