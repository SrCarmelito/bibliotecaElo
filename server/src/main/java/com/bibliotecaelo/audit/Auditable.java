package com.bibliotecaelo.audit;

import com.bibliotecaelo.audit.domain.AuditInfo;

public interface Auditable {

    AuditInfo getAudit();
    void setAudit(AuditInfo auditInfo);

}
