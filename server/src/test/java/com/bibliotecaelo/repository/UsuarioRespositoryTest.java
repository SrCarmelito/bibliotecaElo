package com.bibliotecaelo.repository;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {
        "/sql/usuario.sql"
})
class UsuarioRespositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Test
    void save() {
        Usuario usuario = repository.save(UsuarioFixtures.usuarioPele());

        assertThat(usuario.getId()).isNotNull();
        assertThat(usuario.getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(usuario.getLogin()).isEqualTo("pele");
        assertThat(usuario.getDataNascimento()).isEqualTo(LocalDate.of(1962, 9, 14));
    }

    @Test
    void update() {
        Usuario usuarioToUpdate = repository.findById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115")).orElseThrow();

        usuarioToUpdate.setNome("Nome Modificado");
        usuarioToUpdate.setEmail("modified@modified.com");
        usuarioToUpdate.setLogin("modified");
        usuarioToUpdate.setDataNascimento(LocalDate.of(2024, 11, 15));

        Usuario usuarioUpdated = repository.saveAndFlush(usuarioToUpdate);

        assertThat(usuarioUpdated.getId()).isEqualTo(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115"));
        assertThat(usuarioUpdated.getNome()).isEqualTo("Nome Modificado");
        assertThat(usuarioUpdated.getEmail()).isEqualTo("modified@modified.com");
        assertThat(usuarioUpdated.getLogin()).isEqualTo("modified");
        assertThat(usuarioUpdated.getDataNascimento()).isEqualTo(LocalDate.of(2024, 11, 15));
    }

    @Test
    void findById() {
        Usuario usuarioFinded = repository.findById(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34")).orElseThrow();

        assertThat(usuarioFinded.getId()).isEqualTo(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"));
        assertThat(usuarioFinded.getNome()).isEqualTo("Ozzy Osbourne");
        assertThat(usuarioFinded.getEmail()).isEqualTo("ozzy.osbourne@gmail.com");
        assertThat(usuarioFinded.getLogin()).isEqualTo("ozzy");
    }

    @Test
    void deleteById() {
        assertThat(repository.existsById(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"))).isTrue();

        repository.deleteById(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"));

        assertThat(repository.existsById(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"))).isFalse();
    }

    @Test
    void findByLogin() {
        Usuario usuario = repository.findByLogin("junior");

        assertThat(usuario.getNome()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuario.getEmail()).isEqualTo("carmelito.benali@gmail.com");
        assertThat(usuario.getSituacao()).isEqualTo(SituacaoUsuarioEnum.ATIVO);
        assertThat(usuario.getTelefone()).isEqualTo("44988080437");
    }

    @Test
    void findByEmail() {
         Usuario usuario = repository.findByEmail("carmelito.benali@gmail.com").orElseThrow();

        assertThat(usuario.getNome()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuario.getEmail()).isEqualTo("carmelito.benali@gmail.com");
        assertThat(usuario.getSituacao()).isEqualTo(SituacaoUsuarioEnum.ATIVO);
        assertThat(usuario.getTelefone()).isEqualTo("44988080437");
    }

    @Test
    void findByResetToken() {
         Usuario usuario = repository.findByResetToken(
                 "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdW5pb3IiLCJpZCI6ImVlNGFlODgwLWE0ZGItNDU2My1iMzMwLTdlMmEyN2QyNjExNSIsImV4cCI6MTczMTUwODE0Nn0.5HCyVCE5Ige4aFDjywk7tpHz_j0pYSpE6mye9VXyujc").orElseThrow();

        assertThat(usuario.getNome()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuario.getEmail()).isEqualTo("carmelito.benali@gmail.com");
        assertThat(usuario.getSituacao()).isEqualTo(SituacaoUsuarioEnum.ATIVO);
        assertThat(usuario.getTelefone()).isEqualTo("44988080437");
    }

    @Test
    void existsByLogin() {
        assertThat(repository.existsByLogin("junior")).isTrue();
        assertThat(repository.existsByLogin("123")).isFalse();
    }

    @Test
    void existsByEmail() {
        assertThat(repository.existsByEmail("ozzy.osbourne@gmail.com")).isTrue();
        assertThat(repository.existsByEmail("123")).isFalse();
    }

}
