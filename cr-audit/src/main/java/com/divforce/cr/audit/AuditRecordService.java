package com.divforce.cr.audit;

import com.divforce.cr.audit.repository.AuditActionType;
import com.divforce.cr.audit.repository.AuditOperationType;
import com.divforce.cr.audit.repository.AuditRecord;

import java.util.Map;

/**
 * @author Oscar Makala
 */
public interface AuditRecordService {
    AuditRecord populateAndSaveAuditRecordUsingMapData(AuditOperationType campaign, AuditActionType update, Map<String, Object> mapAuditData);

    AuditRecord populateAndSaveAuditRecord(AuditOperationType auditOperationType, AuditActionType auditActionType, String dataAsString);
}
