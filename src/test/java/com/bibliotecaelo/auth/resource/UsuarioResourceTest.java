package com.bibliotecaelo.auth.resource;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.auth.dto.NewPasswordDTO;
import com.bibliotecaelo.auth.service.UsuarioService;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioResourceTest
        extends DefaultTest {

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void newUser() throws Exception {
        mockMvc.perform(post("/api/usuarios/novo-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UsuarioFixtures.usuarioCarmelitoDTO())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(usuarioService).novoUsuario(any());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void resetPassword() throws Exception {
        mockMvc.perform(post("/api/usuarios/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("carmelito.benali@hotmail.com")))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(usuarioService).resetPassword(any(), any());
        verifyNoMoreInteractions(usuarioService);

    }

    @Test
    public void confirmResetPassword() throws Exception {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setSenha("123");
        newPasswordDTO.setSenhaConfirmacao("123");
        newPasswordDTO.setToken("123");

        mockMvc.perform(post("/api/usuarios/confirm-reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(usuarioService).confirmResetPassword(any());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void findById() throws Exception {

        mockMvc.perform(get("/api/usuarios/{usuarioId}", "ee4ae880-a4db-4563-b330-7e2a27d26115")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(usuarioService).findById(any());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(put("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UsuarioFixtures.usuarioCarmelitoDTO())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(usuarioService).update(any());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void deleteByUsuarioId() throws Exception{
        mockMvc.perform(delete("/api/usuarios/{usuarioId}", "ee4ae880-a4db-4563-b330-7e2a27d26115")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(usuarioService).deleteById(any());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void login() throws Exception{
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("junior");
        loginDTO.setSenha("123");

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(usuarioService).gerarToken(any());
        verifyNoMoreInteractions(usuarioService);

    }



}