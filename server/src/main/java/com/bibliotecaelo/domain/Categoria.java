package com.bibliotecaelo.domain;

import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.domain.AuditInfo;
import com.bibliotecaelo.interfaces.Auditable;
import com.bibliotecaelo.interfaces.Entidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.UUID;

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
