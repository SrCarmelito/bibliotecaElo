package com.bibliotecaelo.domain;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.audit.AuditInfo;
import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.Auditable;
import com.bibliotecaelo.enums.CategoriaLivroEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "livro", schema = "biblioteca")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Audited
@EntityListeners(AuditListener.class)
public class Livro implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(max = 1000)
    @NotBlank(message = "É Necessário Informar o Título do Livro!")
    private String titulo;

    @Size(max = 1000)
    @NotBlank(message = "É Necessário Informar o Autor do Livro!")
    private String autor;

    @Size(max = 13)
    @NotNull(message = "É Necessário Informar o código ISBN do Livro!")
    private Long isbn;

    @Column(name = "data_publicacao")
    @NotNull(message = "É Necessário Informar a Data de Publicação do Livro!")
    private LocalDate dataPublicacao;

    @NotNull(message = "É Necessário Informar a Categoria do Livro!")
    @Enumerated(EnumType.STRING)
    private CategoriaLivroEnum categoria;

    @Embedded
    @NotAudited
    private AuditInfo audit = AuditInfo.now();
}
