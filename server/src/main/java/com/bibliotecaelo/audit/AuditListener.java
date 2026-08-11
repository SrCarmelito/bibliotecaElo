package com.bibliotecaelo.audit;

import com.bibliotecaelo.audit.domain.AuditInfo;
import com.bibliotecaelo.interfaces.Auditable;
import com.bibliotecaelo.utils.AuditUserUtil;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.isNull;

public class AuditListener {

    AuditUserUtil auditUserUtil = new AuditUserUtil();

    @PrePersist
    public void setCreatedOn(Auditable auditable) {

        final AuditInfo audit = Optional.ofNullable(auditable.getAudit()).orElse(new AuditInfo());
        final String auditUserName = auditUserUtil.getAuditUser().getNome();

        audit.setUsuarioCriacao(auditUserName);
        audit.setUsuarioAlteracao(auditUserName);
        audit.setDataCriacao(LocalDateTime.now());
        audit.setDataAlteracao(LocalDateTime.now());

        auditable.setAudit(audit);
    }

    @PreUpdate
    public void setUpdatedOn(Auditable auditable) {

        final String auditUserName = auditUserUtil.getAuditUser().getNome();

        if (isNull(auditable.getAudit())) {
            setCreatedOn(auditable);
        } else {
            auditable.getAudit().setUsuarioAlteracao(auditUserName);
            auditable.getAudit().setDataAlteracao(LocalDateTime.now());
        }
    }

}

