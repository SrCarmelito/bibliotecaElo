package com.bibliotecaelo.auth.converter;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.auth.dto.UsuarioResponseDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsuarioResponseDTOConverterTest extends DefaultTest {

    @Autowired
    UsuarioResponseDTOConverter usuarioResponseDTOConverter;

    @Test
    void from() {
        assertThrows(NotImplementedException.class,
                () -> usuarioResponseDTOConverter.from(UsuarioFixtures.usuarioResponseDTOAlexMartin()));
    }

    @Test
    void to() {
        UsuarioResponseDTO usuarioResponseDTO = usuarioResponseDTOConverter.to(UsuarioFixtures.usuarioPele());

        assertThat(usuarioResponseDTO.getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(usuarioResponseDTO.getEmail()).isEqualTo("carmelito.benali@hotmail.com");
        assertThat(usuarioResponseDTO.getTelefone()).isEqualTo("4499999999");
    }

}
