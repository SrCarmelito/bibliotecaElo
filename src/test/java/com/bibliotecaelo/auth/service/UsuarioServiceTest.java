package com.bibliotecaelo.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.auth.converter.UsuarioDTOConverter;
import com.bibliotecaelo.auth.converter.UsuarioResponseDTOConverter;
import com.bibliotecaelo.auth.domain.Usuario;
import com.bibliotecaelo.auth.dto.UsuarioDTO;
import com.bibliotecaelo.auth.dto.UsuarioResponseDTO;
import com.bibliotecaelo.auth.repository.UsuarioRepository;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioServiceTest extends DefaultTest {

    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    TokenService tokenService;

    @Mock
    EmailService emailService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UsuarioDTOConverter usuarioDTOConverter;

    @Mock
    UsuarioResponseDTOConverter usuarioResponseDTOConverter;

    UsuarioDTO usuarioDTO = UsuarioFixtures.usuarioCarmelitoDTO();

    Usuario usuario = UsuarioFixtures.usuarioPele();

    @Test
    void novoUsuario() {
        when(usuarioDTOConverter.from(usuarioDTO)).thenReturn(usuario);
        when(passwordEncoder.encode(any())).thenReturn("123");
        when(usuarioRepository.save(any())).thenReturn(usuario);

        usuarioService.novoUsuario(usuarioDTO);

        verify(usuarioRepository).findByLogin(any());
        verify(usuarioRepository).findByEmail(any());
        verify(usuarioRepository).save(any());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void resetPassword() throws Exception{
        int EXPIRATION_TIME_LOGIN = 120;

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.saveAndFlush(any())).thenReturn(usuario);
        when(tokenService.gerarToken(usuario, EXPIRATION_TIME_LOGIN)).thenReturn("123");
        when(emailService.enviarEmail(any(), any(), any())).thenReturn("123");

        usuarioService.resetPassword(servletRequest, "carmelito.benali@hotmail.com");

        verify(usuarioRepository).findByEmail(any());
        verify(usuarioRepository).saveAndFlush(any());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void validaUsuario() {
        when(usuarioRepository.findByLogin(usuarioDTO.getLogin())).thenReturn(new Usuario());
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.of(new Usuario()));

        assertThrows(ValidationException.class,
                () -> usuarioService.validaUsuario(usuarioDTO))
                .getMessage().equals("Usuário já existe, tente novamente!");

        assertThrows(ValidationException.class,
                () -> usuarioService.validaUsuario(usuarioDTO))
                .getMessage().equals("E-mail já cadastrado, tente novamente!");

        usuarioDTO.setEmail("aaa");
        assertThrows(ValidationException.class,
                () -> usuarioService.validaUsuario(usuarioDTO))
                .getMessage().equals("Não é um E-mail Válido!");
    }

    @Test
    void validaSenha() {
        usuarioDTO.setSenha("Aa");
        assertThrows(ValidationException.class,
                () -> usuarioService.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao()))
                .getMessage().equals("Senha deve conter entre 6 e 150 caracteres sendo ao menos 1 letra maiúscula, 1 minúscula e 1 número!");

        assertThrows(ValidationException.class,
                () -> usuarioService.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao()))
                .getMessage().equals("Senha e Senha de Confirmação não Conferem, tente novamente!");

    }

    @Test
    void findById() {
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(new Usuario()));
        when(usuarioResponseDTOConverter.to(any())).thenReturn(UsuarioFixtures.usuarioResponseDTOAlexMartin());

        UsuarioResponseDTO usuarioResponseDTO = usuarioService.findById(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"));

        assertThat(usuarioResponseDTO.getNome()).isEqualTo("Alex Martin");
        assertThat(usuarioResponseDTO.getTelefone()).isEqualTo("4533568875");
    }

    @Test
    void deleteById() {
        UUID usuarioId = UUID.randomUUID();

        usuarioService.deleteById(usuarioId);

        verify(usuarioRepository).deleteById(usuarioId);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void findAll() {
        Pageable pageRequest = PageRequest.of(0, 10);
        when(usuarioRepository.findAll(eq(pageRequest))).thenReturn(
                new PageImpl<Usuario>(List.of(UsuarioFixtures.usuarioPele())));

        Page<UsuarioResponseDTO> result = usuarioService.findAll(pageRequest);

        assertThat(result).isNotNull();
        verify(usuarioRepository).findAll(eq(pageRequest));
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void update() {
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(new Usuario()));

        usuarioService.update(usuarioDTO);

        verify(usuarioRepository).findById(usuarioDTO.getId());
        verify(usuarioRepository).saveAndFlush(any(Usuario.class));
        verifyNoMoreInteractions(usuarioRepository);
    }

}