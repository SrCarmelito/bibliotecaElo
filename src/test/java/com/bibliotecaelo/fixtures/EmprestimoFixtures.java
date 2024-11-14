package com.bibliotecaelo.fixtures;

import java.time.LocalDate;

import com.bibliotecaelo.domain.Emprestimo;
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
}
