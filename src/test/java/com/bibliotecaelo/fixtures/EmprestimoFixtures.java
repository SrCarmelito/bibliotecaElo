package com.bibliotecaelo.fixtures;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.dto.Devolucao;
import com.bibliotecaelo.dto.EmprestimoDTO;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;

public class EmprestimoFixtures {

    public static Emprestimo EmprestimoValido() {
        Emprestimo emprestimo = new Emprestimo();

        emprestimo.setUsuario(UsuarioFixtures.usuarioPele());
        emprestimo.setLivro(LivroFixtures.LivroOProcesso());
        emprestimo.setDataEmprestimo(LocalDate.of(2021, 12, 8));
        emprestimo.setDataDevolucao(LocalDate.of(2024, 8, 7));
        emprestimo.setStatus(StatusEmprestimoEnum.CONCLUIDO);

        return emprestimo;
    }

    public static EmprestimoDTO EmprestimoDTOTeste() {
        EmprestimoDTO emprestimoDTO = new EmprestimoDTO();

        emprestimoDTO.setUsuario(UsuarioFixtures.usuarioResponseDTOAlexMartin());
        emprestimoDTO.setLivro(LivroFixtures.LivroDTOOCortico());
        emprestimoDTO.setDataEmprestimo(LocalDate.of(2024, 11, 15));
        emprestimoDTO.setDataDevolucao(LocalDate.of(2025, 12, 7));
        emprestimoDTO.setStatus(StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);

        return emprestimoDTO;
    }

    public static Devolucao emprestimoAtualizadoDTO() {
        Devolucao dto = new Devolucao();

        dto.setId(UUID.fromString("a10f5489-8230-40d3-963f-da31500aa112"));
        dto.setStatus(StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);
        dto.setDataDevolucao(LocalDate.of(2024, 10, 11));

        return dto;
    }
}
