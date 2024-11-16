package com.bibliotecaelo.fixtures;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.auth.domain.Usuario;
import com.bibliotecaelo.auth.dto.UsuarioDTO;
import com.bibliotecaelo.auth.dto.UsuarioResponseDTO;

public class UsuarioFixtures {

    public static Usuario usuarioPele() {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"));
        usuario.setNome("Edson Arantes do Nascimento");
        usuario.setEmail("carmelito.benali@ig.com");
        usuario.setDataCadastro(LocalDate.of(1962, 9, 14));
        usuario.setTelefone("4499999999");
        usuario.setLogin("pele");
        usuario.setSenha("peleCopa64");

        return usuario;
    }

    public static UsuarioDTO usuarioCarmelitoDTO() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(UUID.fromString("ed9871bd-6b33-4199-a00c-3727d8d89748"));
        usuarioDTO.setNome("Carmelito Junior");
        usuarioDTO.setEmail("carmelito.benali@hotmail.com");
        usuarioDTO.setDataCadastro(LocalDate.of(2022, 11, 16));
        usuarioDTO.setTelefone("44988080437");
        usuarioDTO.setLogin("carmelindo");
        usuarioDTO.setSenha("Carmelindo2024");
        usuarioDTO.setSenhaConfirmacao("Carmelindo2024");

        return usuarioDTO;
    }

    public static UsuarioResponseDTO usuarioResponseDTOAlexMartin() {
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(UUID.fromString("054bf7ed-f9ba-4333-98fa-7d700e77526e"));
        usuarioResponseDTO.setNome("Alex Martin");
        usuarioResponseDTO.setEmail("alex.martin@gmail.com");
        usuarioResponseDTO.setDataCadastro(LocalDate.of(2020, 12, 8));
        usuarioResponseDTO.setTelefone("4533568875");
        usuarioResponseDTO.setLogin("alexmartin");

        return usuarioResponseDTO;

    }
}
