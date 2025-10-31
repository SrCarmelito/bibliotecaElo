package com.bibliotecaelo.repository;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.fixtures.LivroFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {
        "/sql/categoria.sql", "/sql/livro.sql", "/sql/emprestimo.sql"
})
class LivroRepositoryTest {

    @Autowired
    LivroRepository repository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Test
    void save() {
        Livro livro = LivroFixtures.LivroOProcesso();
        livro.setCategoria(categoriaRepository.findById(UUID.fromString("58aa185e-51ee-4120-bcae-c53fb5b74d5e")).orElseThrow());

        Livro livroSaved = repository.saveAndFlush(livro);

        assertThat(livroSaved.getId()).isNotNull();
        assertThat(livroSaved.getTitulo()).isEqualTo("O Processo");
        assertThat(livroSaved.getAutor()).isEqualTo("Franz Kakfa");
        assertThat(livroSaved.getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));

        assertThat(livroSaved.getCategoria().getId()).isEqualTo(UUID.fromString("58aa185e-51ee-4120-bcae-c53fb5b74d5e"));
        assertThat(livroSaved.getCategoria().getDescricao()).isEqualTo("Ficção Científica");
    }

    @Test
    @WithMockUser
    void update() {
        Livro livroParaAtualizar = repository.findById(UUID.fromString("de1c8bd9-755d-4f02-9c9b-781c25674109")).orElseThrow();

        livroParaAtualizar.setCategoria(categoriaRepository.findById(UUID.fromString("58aa185e-51ee-4120-bcae-c53fb5b74d5e")).orElseThrow());
        livroParaAtualizar.setTitulo("Titulo Atualizado no Teste!!!");
        livroParaAtualizar.setDataPublicacao(LocalDate.of(1995, 12, 28));
        livroParaAtualizar.setAutor("Autor Modificado");

        Livro livroAtualizado = repository.saveAndFlush(livroParaAtualizar);

        assertThat(livroAtualizado.getTitulo()).isEqualTo("Titulo Atualizado no Teste!!!");
        assertThat(livroAtualizado.getAutor()).isEqualTo("Autor Modificado");
        assertThat(livroAtualizado.getDataPublicacao()).isEqualTo(LocalDate.of(1995, 12, 28));

        assertThat(livroAtualizado.getCategoria().getId()).isEqualTo(UUID.fromString("58aa185e-51ee-4120-bcae-c53fb5b74d5e"));
        assertThat(livroAtualizado.getCategoria().getDescricao()).isEqualTo("Ficção Científica");
    }

    @Test
    void findById() {
        Livro livroFinded = repository.findById(UUID.fromString("8bf07126-eaa2-4207-b3de-cbc7a43e038f")).orElseThrow();

        assertThat(livroFinded.getId()).isEqualTo(UUID.fromString("8bf07126-eaa2-4207-b3de-cbc7a43e038f"));
        assertThat(livroFinded.getTitulo()).isEqualTo("As areias do Tempo");
        assertThat(livroFinded.getAutor()).isEqualTo("Sidney Sheldon");
        assertThat(livroFinded.getDataPublicacao()).isEqualTo(LocalDate.of(1947, 5, 20));

        assertThat(livroFinded.getCategoria().getId()).isEqualTo(UUID.fromString("51f797f6-23f3-4482-8423-cc7a06004486"));
        assertThat(livroFinded.getCategoria().getDescricao()).isEqualTo("Humor");
    }

    @Test
    void deleteById() {
        repository.deleteById(UUID.fromString("8bf07126-eaa2-4207-b3de-cbc7a43e038f"));

        assertThat(repository.findAll()).extracting(Livro::getId)
                .doesNotContain(UUID.fromString("8bf07126-eaa2-4207-b3de-cbc7a43e038f"));
    }

    @Test
    void existsByTitulo() {
        assertThat(repository.existsByTitulo("As areias do Tempo")).isTrue();
        assertThat(repository.existsByTitulo("False")).isFalse();
    }

    @Test
    void existsByIsbn() {
        assertThat(repository.existsByIsbn("4475598957534")).isTrue();
        assertThat(repository.existsByIsbn("1234")).isFalse();
    }

    @Test
    void getRecomendacoes() {
        assertThat(repository.getRecomendacoes(
                UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"),
                Pageable.unpaged()))
        .extracting(Livro::getTitulo)
        .containsOnlyOnce("As areias do Tempo");
    }

}
