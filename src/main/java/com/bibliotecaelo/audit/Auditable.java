package com.bibliotecaelo.audit;

public interface Auditable {

    AuditInfo getAudit();
    void setAudit(AuditInfo auditInfo);

}
