package com.bibliotecaelo.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmprestimoAtualizadoDTO {

    private UUID id;

    @NotNull(message = "É Necessário Informar a Data de Devolução do Livro!")
    private LocalDate dataDevolucao;

    @NotNull(message = "É Necessário Informar a Status do Livro!")
    private StatusEmprestimoEnum status;

}
