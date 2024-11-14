package com.bibliotecaelo.domain;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.audit.AuditInfo;
import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.Auditable;
import com.bibliotecaelo.enums.StatusLivroEnum;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

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

    @PastOrPresent(message = "Data de Empréstimo não pode ser futura!")
    @NotNull(message = "É Necessário Informar a Data de Empréstimo do Livro!")
    private LocalDate dataEmprestimo;

    @NotNull(message = "É Necessário Informar a Data de Devolução do Livro!")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "É Necessário Informar a Status do Livro!")
    private StatusLivroEnum status;

    @Embedded
    @Audited
    private AuditInfo audit = AuditInfo.now();
}
