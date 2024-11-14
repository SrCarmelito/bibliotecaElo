package com.bibliotecaelo.domain;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.audit.AuditInfo;
import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.Auditable;
import com.bibliotecaelo.auth.domain.Usuario;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "emprestimo", schema = "biblioteca")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Audited
@EntityListeners(AuditListener.class)
public class Emprestimo implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @NotNull(message = "É Necessário Informar o usuário!")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    @NotNull(message = "É Necessário Informar o Livro!")
    private Livro livro;

    @Column(name = "data_emprestimo")
    @PastOrPresent(message = "Data de Empréstimo não pode ser futura!")
    @NotNull(message = "É Necessário Informar a Data de Empréstimo do Livro!")
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao")
    @NotNull(message = "É Necessário Informar a Data de Devolução do Livro!")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "É Necessário Informar a Status do Livro!")
    private StatusEmprestimoEnum status;

    @Embedded
    @NotAudited
    private AuditInfo audit = AuditInfo.now();
}
