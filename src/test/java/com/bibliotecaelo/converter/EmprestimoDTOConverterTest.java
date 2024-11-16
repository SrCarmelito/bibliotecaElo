package com.bibliotecaelo.converter;

import java.time.LocalDate;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.dto.EmprestimoDTO;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.fixtures.EmprestimoFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class EmprestimoDTOConverterTest extends DefaultTest {

    @Autowired
    EmprestimoDTOConverter converter;

    @Test
    void from() {
        Emprestimo emprestimo = converter.from(EmprestimoFixtures.EmprestimoDTOTeste());

        assertThat(emprestimo.getDataEmprestimo()).isEqualTo(LocalDate.of(2024, 11, 15));
        assertThat(emprestimo.getDataDevolucao()).isEqualTo(LocalDate.of(2025, 12, 7));
        assertThat(emprestimo.getStatus()).isEqualTo(StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);
    }

    @Test
    void to() {
        EmprestimoDTO emprestimoDTO = converter.to(EmprestimoFixtures.EmprestimoValido());

        assertThat(emprestimoDTO.getUsuario().getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(emprestimoDTO.getLivro().getAutor()).isEqualTo("Franz Kakfa");

        assertThat(emprestimoDTO.getDataEmprestimo()).isEqualTo(LocalDate.of(2021, 12, 8));
        assertThat(emprestimoDTO.getDataDevolucao()).isEqualTo(LocalDate.of(2024, 8, 7));
        assertThat(emprestimoDTO.getStatus()).isEqualTo(StatusEmprestimoEnum.CONCLUIDO);

    }
}