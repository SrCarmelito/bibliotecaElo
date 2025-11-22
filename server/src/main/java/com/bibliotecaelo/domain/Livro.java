package com.bibliotecaelo.domain;

import java.time.LocalDate;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "livro", schema = "biblioteca")
@Data
@Audited
@EntityListeners(AuditListener.class)
public class Livro implements Auditable, Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    @Size(max = 1000)
    @NotBlank(message = "É necessário informar o título do livro.")
    private String titulo;

    @Size(max = 1000)
    @NotBlank(message = "É necessário informar o autor do livro.")
    private String autor;

    @Column(unique = true)
    @Size(max = 13)
    @NotBlank(message = "É necessário informar o código ISBN do livro.")
    private String isbn;

    @Column(name = "data_publicacao")
    @NotNull(message = "É necessário informar a data de publicação do livro.")
    private LocalDate dataPublicacao;

    @ManyToOne
    @NotNull(message = "É necessário informar a categoria do livro.")
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Embedded
    @NotAudited
    private AuditInfo audit = AuditInfo.now();
}
