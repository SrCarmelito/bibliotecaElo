package com.bibliotecaelo.repository;

import java.time.LocalDate;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.auth.repository.UsuarioRepository;
import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.enums.CategoriaLivroEnum;
import com.bibliotecaelo.fixtures.EmprestimoFixtures;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class EmprestimoRepositoryTest extends DefaultTest {

    @Autowired
    EmprestimoRepository emprestimoRepository;

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    private Emprestimo emprestimo() {
        Emprestimo emprestimo = EmprestimoFixtures.EmprestimoValido();
        emprestimo.setLivro(livroRepository.save(LivroFixtures.LivroOProcesso()));
        emprestimo.setUsuario(usuarioRepository.save(UsuarioFixtures.usuarioPele()));

        return emprestimoRepository.save(emprestimo);
    }

    @Test
    void save() {
        Emprestimo emprestimoSalvo = emprestimo() ;

        assertThat(emprestimoSalvo.getLivro().getAutor()).isEqualTo("Franz Kakfa");
        assertThat(emprestimoSalvo.getLivro().getCategoria()).isEqualTo(CategoriaLivroEnum.DISTOPIA);
        assertThat(emprestimoSalvo.getUsuario().getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(emprestimoSalvo.getUsuario().getEmail()).isEqualTo("carmelito.benali@hotmail.com");
        assertThat(emprestimoSalvo.getDataDevolucao()).isEqualTo(LocalDate.of(2024, 8, 7));

    }

    @Test
    void findById() {
        Emprestimo emprestimo = emprestimo() ;

        Emprestimo emprestimoBuscado = emprestimoRepository.findById(emprestimo.getId()).orElseThrow();
        assertThat(emprestimoBuscado.getLivro().getAutor()).isEqualTo("Franz Kakfa");
        assertThat(emprestimoBuscado.getLivro().getCategoria()).isEqualTo(CategoriaLivroEnum.DISTOPIA);
        assertThat(emprestimoBuscado.getUsuario().getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(emprestimoBuscado.getUsuario().getEmail()).isEqualTo("carmelito.benali@hotmail.com");
        assertThat(emprestimoBuscado.getDataDevolucao()).isEqualTo(LocalDate.of(2024, 8, 7));
    }

    @Test
    void update() {
        Emprestimo emprestimo = emprestimo() ;
        Emprestimo emprestimoParaAtualizar = emprestimoRepository.findById(emprestimo.getId()).orElseThrow();

        emprestimoParaAtualizar.setDataEmprestimo(LocalDate.of(1995, 12, 28));
        emprestimoParaAtualizar.setDataDevolucao(LocalDate.of(2020, 5, 5));

        Emprestimo emprestimoAtualizado = emprestimoRepository.save(emprestimoParaAtualizar);

        assertThat(emprestimoAtualizado.getDataEmprestimo()).isEqualTo(LocalDate.of(1995, 12, 28));
        assertThat(emprestimoAtualizado.getDataDevolucao()).isEqualTo(LocalDate.of(2020, 5, 5));
    }


}
