package com.bibliotecaelo.auth.service;

import java.util.Optional;

import com.bibliotecaelo.auth.dto.EmailDTO;
import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.auth.dto.NewPasswordDTO;
import com.bibliotecaelo.auth.validations.UserValidations;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LoginServiceTest {

    @InjectMocks
    LoginService loginService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    TokenService tokenService;

    @Mock
    EmailService emailService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserValidations userValidations;

    @BeforeEach
    void setUp(){
        usuario = UsuarioFixtures.usuarioPele();
        emailDTO = new EmailDTO();
        emailDTO.setEmail("carmelito.benali@ig.com");
    }

    Usuario usuario;
    EmailDTO emailDTO;

    @Test
    void resetPassword() throws Exception {
        int EXPIRATION_INT_MINUTES_NEW_PASSWORD = 5;

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        when(usuarioRepository.findByEmail(emailDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.saveAndFlush(usuario)).thenReturn(usuario);
        when(tokenService.gerarToken(usuario, EXPIRATION_INT_MINUTES_NEW_PASSWORD)).thenReturn("123");
        when(emailService.enviarEmail(eq(emailDTO.getEmail()), eq("Carmelito - App"), any(String.class))).thenReturn("123");

        loginService.resetPassword(servletRequest, emailDTO);

        verify(usuarioRepository).findByEmail(any());
        verify(usuarioRepository).saveAndFlush(any());
        verify(tokenService).gerarToken(usuario, EXPIRATION_INT_MINUTES_NEW_PASSWORD);
        verify(emailService).enviarEmail(eq(emailDTO.getEmail()), eq("Carmelito - App"), any(String.class));
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void resetPasswordValidaEmail() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        String mensagemEmailNaoCadastrado = assertThrows(IllegalArgumentException.class,
                () -> loginService.resetPassword(servletRequest, emailDTO))
                .getMessage();

        assertThat(mensagemEmailNaoCadastrado).isEqualTo("Não corresponde a um e-mail cadastrado.");
    }

    @Test
    void confirmResetPassword() {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setToken("1234");
        newPasswordDTO.setSenha("123Carmelito");
        newPasswordDTO.setSenhaConfirmacao("123Carmelito");

        when(tokenService.getSubject(newPasswordDTO.getToken())).thenReturn("123");
        when(usuarioRepository.findByResetToken(newPasswordDTO.getToken())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(newPasswordDTO.getSenha())).thenReturn("123456");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        loginService.confirmResetPassword(newPasswordDTO);

        verify(tokenService).getSubject(newPasswordDTO.getToken());
        verify(usuarioRepository).findByResetToken(newPasswordDTO.getToken());
        verify(userValidations).validaSenha(newPasswordDTO.getSenha(), newPasswordDTO.getSenhaConfirmacao());
        verify(passwordEncoder).encode(newPasswordDTO.getSenha());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void confirmResetPasswordInvalidToken() {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setToken("1234");
        newPasswordDTO.setSenha("123Carmelito");
        newPasswordDTO.setSenhaConfirmacao("123Carmelito");

        when(tokenService.getSubject(newPasswordDTO.getToken())).thenThrow();

        assertThrows(ValidationException.class, () -> loginService.confirmResetPassword(newPasswordDTO));

        verify(tokenService).getSubject(newPasswordDTO.getToken());
    }

    @Test
    void confirmResetPasswordResetTokenNotFound() {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setToken("1234");
        newPasswordDTO.setSenha("123Carmelito");
        newPasswordDTO.setSenhaConfirmacao("123Carmelito");

        assertThrows(ValidationException.class, () -> loginService.confirmResetPassword(newPasswordDTO));

        verify(tokenService).getSubject(newPasswordDTO.getToken());
        verify(usuarioRepository).findByResetToken(newPasswordDTO.getToken());
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoMoreInteractions(tokenService);
    }

    @Test
    void gerarTokenUsuarioNaoEncontrado() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("junior");
        loginDTO.setSenha("1234567");

        when(usuarioRepository.existsByLogin("junior")).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> loginService.gerarToken(loginDTO));
    }

}
