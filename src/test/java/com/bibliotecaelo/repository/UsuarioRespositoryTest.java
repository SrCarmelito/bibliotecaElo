package com.bibliotecaelo.repository;

import java.time.LocalDate;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {
        "/sql/usuario.sql"
})
@Transactional
class UsuarioRespositoryTest extends DefaultTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    void save() {
        Usuario usuario = usuarioRepository.save(UsuarioFixtures.usuarioPele());

        assertThat(usuario.getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(usuario.getLogin()).isEqualTo("pele");
        assertThat(usuario.getDataCadastro()).isEqualTo(LocalDate.of(1962, 9, 14));
    }

    @Test
    void findByLogin() {
        Usuario usuario = usuarioRepository.findByLogin("junior");

        assertThat(usuario.getNome()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuario.getEmail()).isEqualTo("carmelito.benali@gmail.com");
        assertThat(usuario.getSituacao()).isEqualTo(SituacaoUsuarioEnum.ATIVO);
        assertThat(usuario.getTelefone()).isEqualTo("44988080437");
    }

    @Test
    void findByEmail() {
         Usuario usuario = usuarioRepository.findByEmail("carmelito.benali@gmail.com").orElseThrow();

        assertThat(usuario.getNome()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuario.getEmail()).isEqualTo("carmelito.benali@gmail.com");
        assertThat(usuario.getSituacao()).isEqualTo(SituacaoUsuarioEnum.ATIVO);
        assertThat(usuario.getTelefone()).isEqualTo("44988080437");
    }

    @Test
    void findByResetToken() {
         Usuario usuario = usuarioRepository.findByResetToken(
                 "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdW5pb3IiLCJpZCI6ImVlNGFlODgwLWE0ZGItNDU2My1iMzMwLTdlMmEyN2QyNjExNSIsImV4cCI6MTczMTUwODE0Nn0.5HCyVCE5Ige4aFDjywk7tpHz_j0pYSpE6mye9VXyujc").orElseThrow();

        assertThat(usuario.getNome()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuario.getEmail()).isEqualTo("carmelito.benali@gmail.com");
        assertThat(usuario.getSituacao()).isEqualTo(SituacaoUsuarioEnum.ATIVO);
        assertThat(usuario.getTelefone()).isEqualTo("44988080437");
    }


}
