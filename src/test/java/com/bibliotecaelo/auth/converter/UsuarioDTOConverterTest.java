package com.bibliotecaelo.auth.converter;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.auth.domain.Usuario;
import com.bibliotecaelo.auth.dto.UsuarioDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioDTOConverterTest extends DefaultTest {

    @Autowired
    UsuarioDTOConverter usuarioDTOConverter;

    @Test
    void from() {
        Usuario usuario = usuarioDTOConverter.from(UsuarioFixtures.usuarioCarmelitoDTO());

        assertThat(usuario.getNome()).isEqualTo("Carmelito Junior");
        assertThat(usuario.getEmail()).isEqualTo("carmelito.benali@hotmail.com");
        assertThat(usuario.getTelefone()).isEqualTo("44988080437");
    }

    @Test
    void to() {
        UsuarioDTO usuarioDTO = usuarioDTOConverter.to(UsuarioFixtures.usuarioPele());

        assertThat(usuarioDTO.getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(usuarioDTO.getEmail()).isEqualTo("carmelito.benali@hotmail.com");
        assertThat(usuarioDTO.getTelefone()).isEqualTo("4499999999");
    }


}
