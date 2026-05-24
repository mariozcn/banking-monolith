package com.banking.banking_monolith.audit;


import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog log(AuditAction action,String entityType, Long entityId, String details){
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setDetails(details);
        auditLog.setEntityId(entityId);
        auditLog.setEntityType(entityType);
        auditLog.setDate(Instant.now());

        return auditLogRepository.save(auditLog);
    }

}
