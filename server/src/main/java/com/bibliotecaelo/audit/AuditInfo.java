package com.bibliotecaelo.audit;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Embeddable
@RevisionEntity
public class AuditInfo {

    @Column(name = "usuariocriacao", updatable = false)
    @CreatedBy
    private String usuarioCriacao;

    @NotNull
    @Column(name = "datacriacao", updatable = false)
    @CreatedDate
    private LocalDateTime dataCriacao;

    @Column(name = "usuarioalteracao")
    @LastModifiedBy
    private String usuarioAlteracao;

    @NotNull
    @Column(name = "dataalteracao")
    @LastModifiedDate
    private LocalDateTime dataAlteracao;

    public static AuditInfo now() {
        final AuditInfo audit = new AuditInfo();
        audit.setDataAlteracao(LocalDateTime.now());
        audit.setDataCriacao(LocalDateTime.now());
        return audit;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
        if (this.dataAlteracao == null) {
            this.dataAlteracao = dataCriacao;
        }
    }

    public void setUsuarioCriacao(String usuario) {
        this.usuarioCriacao = usuario;
        if (this.usuarioAlteracao == null || this.usuarioAlteracao.isEmpty()) {
            this.usuarioAlteracao = usuario;
        }
    }

}
