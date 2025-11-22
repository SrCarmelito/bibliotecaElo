package com.bibliotecaelo.domain;

import java.util.UUID;

import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.Auditable;
import com.bibliotecaelo.audit.domain.AuditInfo;
import com.bibliotecaelo.interfaces.Entidade;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Data
@EntityListeners(AuditListener.class)
@Table(name = "categoria", schema = "biblioteca")
public class Categoria implements Auditable, Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    @Size(max = 1000)
    @NotBlank(message = "É necessário informar a descrição da categoria")
    private String descricao;

    @Embedded
    @NotAudited
    private AuditInfo audit = AuditInfo.now();
}
