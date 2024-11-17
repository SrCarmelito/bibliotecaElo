package com.bibliotecaelo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.enums.CategoriaLivroEnum;
import com.bibliotecaelo.fixtures.LivroFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {
        "/sql/livro.sql", "/sql/emprestimo.sql"
})
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

    @Test
    void buscaLivro() {
        Livro livrobuscado = repository.findById(UUID.fromString("8bf07126-eaa2-4207-b3de-cbc7a43e038f")).orElseThrow();
        assertThat(livrobuscado.getIsbn()).isEqualTo(4475598957534L);
    }

    @Test
    void livrosEmprestadosPorUsuarioId() {
        List<Livro> livros = repository.livrosEmprestadosPorUsuarioId(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"));

        assertThat(livros).extracting(Livro::getCategoria)
                .containsExactlyInAnyOrder(
                        CategoriaLivroEnum.FICCAO_POLICIAL,
                        CategoriaLivroEnum.GRAPHIC_NOVEL);
    }

    @Test
    void findAllByCategoria() {
        List<Livro> livros = repository.findAllByCategoria(CategoriaLivroEnum.FICCAO_POLICIAL);

        assertThat(livros).size().isEqualTo(2);
    }

    @Test
    void findAllByIsbn() {
        List<Livro> livros = repository.findAllByIsbn(4475598957534L);

        assertThat(livros).size().isEqualTo(1);
    }

}
