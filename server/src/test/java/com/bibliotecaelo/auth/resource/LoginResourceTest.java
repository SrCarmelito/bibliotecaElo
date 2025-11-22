package com.bibliotecaelo.auth.resource;

import com.bibliotecaelo.ResourceTest;
import com.bibliotecaelo.auth.dto.EmailDTO;
import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.auth.dto.NewPasswordDTO;
import com.bibliotecaelo.auth.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginResourceTest extends ResourceTest {

    @MockBean
    LoginService loginService;

    @Test
    void login() throws Exception{
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("junior");
        loginDTO.setSenha("123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(loginService).gerarToken(loginDTO);
        verifyNoMoreInteractions(loginService);
    }

    @Test
    void resetPassword() throws Exception {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("carmelito.benali@hotmail.com");

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(loginService).resetPassword(any(), any());
        verifyNoMoreInteractions(loginService);
    }

    @Test
    void confirmResetPassword() throws Exception {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setSenha("123");
        newPasswordDTO.setSenhaConfirmacao("123");
        newPasswordDTO.setToken("123");

        mockMvc.perform(post("/api/auth/confirm-reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPasswordDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(loginService).confirmResetPassword(any());
        verifyNoMoreInteractions(loginService);
    }
}
