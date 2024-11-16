package com.bibliotecaelo.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.auth.dto.UsuarioResponseDTO;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class EmprestimoDTO {

    private UUID id;

    @NotNull(message = "É Necessário Informar o usuário!")
    private UsuarioResponseDTO usuario;

    @NotNull(message = "É Necessário Informar o Livro!")
    private LivroDTO livro;

    @PastOrPresent(message = "Data de Empréstimo não pode ser futura!")
    @NotNull(message = "É Necessário Informar a Data de Empréstimo do Livro!")
    private LocalDate dataEmprestimo;

    @NotNull(message = "É Necessário Informar a Data de Devolução do Livro!")
    private LocalDate dataDevolucao;

    @NotNull(message = "É Necessário Informar a Status do Livro!")
    private StatusEmprestimoEnum status;

}
