package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.UsuarioDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioDTOConverterTest  {

    UsuarioDTOConverter usuarioDTOConverter = new UsuarioDTOConverter();

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
        assertThat(usuarioDTO.getEmail()).isEqualTo("carmelito.benali@ig.com");
        assertThat(usuarioDTO.getTelefone()).isEqualTo("4499999999");
    }
}
