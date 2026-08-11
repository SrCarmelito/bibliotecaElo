package com.bibliotecaelo.domain;

import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.domain.AuditInfo;
import com.bibliotecaelo.interfaces.Auditable;
import com.bibliotecaelo.interfaces.Entidade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.io.InputStream;
import java.util.UUID;

@Table(name = "bucketfile", schema = "biblioteca")
@Entity
@Data
@Audited
@EntityListeners(AuditListener.class)
public class BucketFile implements Auditable, Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(max = 1000)
    private String nome;
    
    private UUID fileId;

    private long size;

    private String contentType;

    @Transient
    @JsonIgnore
    private InputStream inputStream;

    @Embedded
    @NotAudited
    private AuditInfo audit = AuditInfo.now();

}