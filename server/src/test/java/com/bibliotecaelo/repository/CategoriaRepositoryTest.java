package com.bibliotecaelo.repository;

import java.util.UUID;

import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.fixtures.CategoriaFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {
        "/sql/categoria.sql"
})
class CategoriaRepositoryTest {

    @Autowired
    CategoriaRepository repository;

    @Test
    void save() {
        Categoria categoriaSaved = repository.saveAndFlush(CategoriaFixtures.CategoriaPolicial());

        assertThat(categoriaSaved.getId()).isNotNull();
        assertThat(categoriaSaved.getDescricao()).isEqualTo("Policial");
    }

    @Test
    void update() {
        Categoria categoriaToUpdate = repository.findById(UUID.fromString("58aa185e-51ee-4120-bcae-c53fb5b74d5e")).orElseThrow();
        categoriaToUpdate.setDescricao("Romance");

        Categoria categoriaUpdated = repository.saveAndFlush(categoriaToUpdate);

        assertThat(categoriaUpdated.getId()).isEqualTo(UUID.fromString("58aa185e-51ee-4120-bcae-c53fb5b74d5e"));
        assertThat(categoriaUpdated.getDescricao()).isEqualTo("Romance");
    }

    @Test
    void findById() {
        Categoria categoriaFinded = repository.findById(UUID.fromString("58aa185e-51ee-4120-bcae-c53fb5b74d5e")).orElseThrow();

        assertThat(categoriaFinded.getId()).isEqualTo(UUID.fromString("58aa185e-51ee-4120-bcae-c53fb5b74d5e"));
        assertThat(categoriaFinded.getDescricao()).isEqualTo("Ficção Científica");
    }

    @Test
    void deleteById() {
        assertThat(repository.existsById(UUID.fromString("51f797f6-23f3-4482-8423-cc7a06004486"))).isTrue();

        repository.deleteById(UUID.fromString("51f797f6-23f3-4482-8423-cc7a06004486"));

        assertThat(repository.existsById(UUID.fromString("51f797f6-23f3-4482-8423-cc7a06004486"))).isFalse();
    }

    @Test
    void existsByDescricao() {
        assertThat(repository.existsByDescricao("Ficção Científica")).isTrue();
        assertThat(repository.existsByDescricao("Humorr")).isFalse();
    }

    @Test
    void existsByDescricaoAndIdNot() {
        assertThat(repository.existsByDescricaoAndIdNot("Ficção Científica", UUID.fromString("51f797f6-23f3-4482-8423-cc7a06004486"))).isTrue();
    }

}
