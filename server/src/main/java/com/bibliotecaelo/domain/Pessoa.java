package com.bibliotecaelo.domain;

import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.domain.AuditInfo;
import com.bibliotecaelo.interfaces.Auditable;
import com.bibliotecaelo.interfaces.Entidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pessoa", schema = "biblioteca")
@Data
@Audited
@EntityListeners(AuditListener.class)
public class Pessoa implements Auditable, Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_integration")
    private Long idIntegration;

    @NotBlank(message = "É Necessário informar o nome!")
    private String nome;

    @NotNull(message = "Não é permitido data de nascimento vazia.")
    @PastOrPresent(message = "Não é permitido data de nascimento no futuro.")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotBlank(message = "É necessário informar o CPF")
    @CPF(message = "CPF inválido! verifique!")
    private String cpf;

    @NotBlank(message = "É Necessário Informar o Telefone!")
    @Size(min = 10, max = 11, message = "Deve ser entre 10 e 11 caracteres com DDD")
    private String telefone;

    @Transient
    private BigDecimal idade;

    @Embedded
    @NotAudited
    private AuditInfo audit = AuditInfo.now();

    public BigDecimal getIdade() {
        return BigDecimal.valueOf(LocalDate.now().getYear())
                .subtract(new BigDecimal(getDataNascimento().getYear()));
    }

}
