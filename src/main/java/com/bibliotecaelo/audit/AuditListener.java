package com.bibliotecaelo.audit;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bibliotecaelo.domain.Usuario;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.isNull;

public class AuditListener {

    @PrePersist
    public void setCreatedOn(Auditable auditable) {

        final AuditInfo audit = Optional.ofNullable(auditable.getAudit()).orElse(new AuditInfo());
        Usuario user = new Usuario();
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String ANONIMOUS_USER = "anonymousUser";
            if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals(ANONIMOUS_USER)) {
                user.setLogin(ANONIMOUS_USER);
                user.setNome(ANONIMOUS_USER);
            } else {
                user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }
        }

        audit.setDataCriacao(LocalDateTime.now());
        audit.setDataAlteracao(LocalDateTime.now());
        if (user == null || user.getNome() == null) {
            audit.setUsuarioCriacao("System");
            audit.setUsuarioAlteracao("System");
        } else {
            audit.setUsuarioCriacao(user.getNome());
            audit.setUsuarioAlteracao(user.getNome());
        }
        auditable.setAudit(audit);
    }

    @PreUpdate
    public void setUpdatedOn(Auditable auditable) {

        if (isNull(auditable.getAudit())) {
            setCreatedOn(auditable);
        } else {
            auditable.getAudit().setUsuarioAlteracao(SecurityContextHolder.getContext().getAuthentication().getName());
            auditable.getAudit().setDataAlteracao(LocalDateTime.now());
        }

    }

}

