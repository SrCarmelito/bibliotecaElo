package com.bibliotecaelo.interfaces;

import com.bibliotecaelo.audit.domain.AuditInfo;

public interface Auditable {

    AuditInfo getAudit();
    void setAudit(AuditInfo auditInfo);

}
