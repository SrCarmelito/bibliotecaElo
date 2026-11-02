package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.UsuarioResponseDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioResponseDTOConverterTest {

    UsuarioResponseDTOConverter usuarioResponseDTOConverter = new UsuarioResponseDTOConverter();

    @Test
    void from() {
        Usuario usuario = usuarioResponseDTOConverter.from(UsuarioFixtures.usuarioResponseDTOAlexMartin());

        assertThat(usuario.getNome()).isEqualTo("Alex Martin");
        assertThat(usuario.getLogin()).isEqualTo("alexmartin");
        assertThat(usuario.getId()).isEqualTo(UUID.fromString("054bf7ed-f9ba-4333-98fa-7d700e77526e"));
    }

    @Test
    void to() {
        UsuarioResponseDTO usuarioResponseDTO = usuarioResponseDTOConverter.to(UsuarioFixtures.usuarioPele());

        assertThat(usuarioResponseDTO.getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(usuarioResponseDTO.getEmail()).isEqualTo("carmelito.benali@ig.com");
        assertThat(usuarioResponseDTO.getTelefone()).isEqualTo("4499999999");
    }

}
