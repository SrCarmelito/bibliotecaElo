package com.bibliotecaelo.repository;

import java.time.LocalDate;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.enums.CategoriaLivroEnum;
import com.bibliotecaelo.fixtures.LivroFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class LivroRepositoryTest extends DefaultTest {

    @Autowired
    LivroRepository repository;

    @Test
    void save() {
        Livro livro = repository.save(LivroFixtures.LivroOProcesso());

        assertThat(livro.getTitulo()).isEqualTo("O Processo");
        assertThat(livro.getAutor()).isEqualTo("Franz Kakfa");
        assertThat(livro.getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(livro.getCategoria()).isEqualTo(CategoriaLivroEnum.DISTOPIA);
    }

    @Test
    void findById() {
        Livro livro = repository.save(LivroFixtures.LivroOProcesso());

        Livro livroBuscado = repository.findById(livro.getId()).orElseThrow();
        assertThat(livroBuscado.getTitulo()).isEqualTo("O Processo");
        assertThat(livroBuscado.getAutor()).isEqualTo("Franz Kakfa");
        assertThat(livroBuscado.getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(livroBuscado.getCategoria()).isEqualTo(CategoriaLivroEnum.DISTOPIA);
    }

    @Test
    void update() {
        Livro livro = repository.save(LivroFixtures.LivroOProcesso());
        Livro livroParaAtualizar = repository.findById(livro.getId()).orElseThrow();

        livroParaAtualizar.setCategoria(CategoriaLivroEnum.ACAO_AVENTURA);
        livroParaAtualizar.setTitulo("Titulo Atualizado no Teste!!!");
        livroParaAtualizar.setDataPublicacao(LocalDate.of(1995, 12, 28));
        livroParaAtualizar.setAutor("Autor Modificado");

        Livro livroAtualizado = repository.save(livroParaAtualizar);

        assertThat(livroAtualizado.getTitulo()).isEqualTo("Titulo Atualizado no Teste!!!");
        assertThat(livroAtualizado.getAutor()).isEqualTo("Autor Modificado");
        assertThat(livroAtualizado.getDataPublicacao()).isEqualTo(LocalDate.of(1995, 12, 28));
        assertThat(livroAtualizado.getCategoria()).isEqualTo(CategoriaLivroEnum.ACAO_AVENTURA);

    }

}