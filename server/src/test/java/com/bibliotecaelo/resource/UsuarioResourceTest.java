package com.bibliotecaelo.resource;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.ResourceTest;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.dto.usuario.UsuarioResponseDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioResourceTest extends ResourceTest {

    @MockBean
    private UsuarioService usuarioService;

    Usuario usuario = UsuarioFixtures.usuarioPele();
    UsuarioDTO usuarioDTO = UsuarioFixtures.usuarioCarmelitoDTO();
    UsuarioResponseDTO usuarioResponseDTO = UsuarioFixtures.usuarioResponseDTOAlexMartin();

    @Test
    void novoUsuario() throws Exception {
        when(usuarioService.novoUsuario(usuarioDTO)).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post("/api/usuarios/novo-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", equalTo("Alex Martin")))
                .andExpect(jsonPath("$.email", equalTo("alex.martin@gmail.com")))
                .andExpect(jsonPath("$.dataCadastro", equalTo("2020-12-08")))
                .andExpect(jsonPath("$.login", equalTo("alexmartin")));

        verify(usuarioService).novoUsuario(usuarioDTO);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void findById() throws Exception {
        when(usuarioService.findById(any())).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/{usuarioId}", "ee4ae880-a4db-4563-b330-7e2a27d26115")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", equalTo("Edson Arantes do Nascimento")))
                .andExpect(jsonPath("$.email", equalTo("carmelito.benali@ig.com")))
                .andExpect(jsonPath("$.login", equalTo("pele")))
                .andExpect(jsonPath("$.dataCadastro", equalTo("1962-09-14")));

        verify(usuarioService).findById(any());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void update() throws Exception {
        when(usuarioService.findById(usuarioDTO.getId())).thenReturn(usuario);
        when(usuarioService.update(usuario)).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("f5070c94-c1ec-4be1-96cf-db855e3c5a1b")))
                .andExpect(jsonPath("$.nome", equalTo("Carmelito Junior")))
                .andExpect(jsonPath("$.email", equalTo("carmelito.benali@hotmail.com")))
                .andExpect(jsonPath("$.login", equalTo("carmelito")))
                .andExpect(jsonPath("$.dataCadastro", equalTo("2022-11-16")));

        verify(usuarioService).update(usuario);
        verify(usuarioService).findById(usuarioDTO.getId());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deleteById() throws Exception{
        mockMvc.perform(delete("/api/usuarios/{usuarioId}", "ee4ae880-a4db-4563-b330-7e2a27d26115")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(usuarioService).deleteById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115"));
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void findAllSearch() throws Exception {
        Page<Usuario> pageToReturn = new PageImpl<>(List.of(usuario));
        Pageable pageable = Pageable.ofSize(20);
        String search = "email=ilike=ozzy";

        when(usuarioService.findByRsql(search, pageable)).thenReturn(pageToReturn);

        mockMvc.perform(get("/api/usuarios/find?search=" + search)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", hasItem("f5070c94-c1ec-4be1-96cf-db855e3c5a1b")))
                .andExpect(jsonPath("$.content[*].nome", hasItem("Edson Arantes do Nascimento")))
                .andExpect(jsonPath("$.content[*].telefone", hasItem("4499999999")))
                .andExpect(jsonPath("$.content[*].email", hasItem("carmelito.benali@ig.com")))
                .andExpect(jsonPath("$.content[*].login", hasItem("pele")));

        verify(usuarioService).findByRsql(search, pageable);
        verifyNoMoreInteractions(usuarioService);
    }

}