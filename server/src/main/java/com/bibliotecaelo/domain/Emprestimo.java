package com.bibliotecaelo.domain;

import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.domain.AuditInfo;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.interfaces.Auditable;
import com.bibliotecaelo.interfaces.Entidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "emprestimo", schema = "biblioteca")
@Data
@Audited
@EntityListeners(AuditListener.class)
public class Emprestimo
        implements Auditable, Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @NotNull(message = "É necessário informar o usuário.")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    @NotNull(message = "É necessário informar o livro.")
    private Livro livro;

    @Column(name = "data_emprestimo")
    @PastOrPresent(message = "Data do empréstimo não pode ser futura.")
    @NotNull(message = "É necessário informar a data de empréstimo do livro.")
    private LocalDate dataEmprestimo;

    @Column(name = "data_devolucao")
    @NotNull(message = "É necessário informar a data de devolução do livro.")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "É necessário informar o status do empréstimo.")
    private StatusEmprestimoEnum status;

    @Embedded
    @NotAudited
    private AuditInfo audit = AuditInfo.now();
}
