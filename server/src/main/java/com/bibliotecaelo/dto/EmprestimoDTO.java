package com.bibliotecaelo.dto;

import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.interfaces.EntidadeDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmprestimoDTO
        implements EntidadeDTO {

    private UUID id;

    @NotNull(message = "É necessário informar o usuário.")
    private UsuarioResponseDTO usuario;

    @NotNull(message = "É necessário informar o livro.")
    private LivroDTO livro;

    @PastOrPresent(message = "Data de empréstimo não pode ser futura.")
    @NotNull(message = "É necessário informar a data de empréstimo do livro.")
    private LocalDate dataEmprestimo;

    @NotNull(message = "É necessário informar a data de devolução do livro.")
    private LocalDate dataDevolucao;

    @NotNull(message = "É necessário informar a status do livro.")
    private StatusEmprestimoEnum status;

}
