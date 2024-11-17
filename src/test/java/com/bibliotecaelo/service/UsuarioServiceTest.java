package com.bibliotecaelo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.auth.service.EmailService;
import com.bibliotecaelo.auth.service.TokenService;
import com.bibliotecaelo.converter.UsuarioDTOConverter;
import com.bibliotecaelo.converter.UsuarioResponseDTOConverter;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.NewPasswordDTO;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.dto.usuario.UsuarioResponseDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.UsuarioRepository;
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
    EmprestimoRepository emprestimoRepository;

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
    void resetPasswordValidaEmail() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());

        String mensagemEmailNaoCadastrado = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.resetPassword(servletRequest, "12345"))
                .getMessage();

        assertThat(mensagemEmailNaoCadastrado).isEqualTo("Não corresponde a um e-mail Cadastrado");
    }

    @Test
    void validaUsuarioExistente() {
        when(usuarioRepository.findByLogin(usuarioDTO.getLogin())).thenReturn(new Usuario());

        String mensagemUsuarioJaExiste = assertThrows(ValidationException.class,
                () -> usuarioService.validaUsuario(usuarioDTO))
                .getMessage();

        assertThat(mensagemUsuarioJaExiste).isEqualTo("Usuário já existe, tente novamente!");
    }

    @Test
    void validaEmailJaCadastrado() {
        when(usuarioRepository.findByLogin(usuarioDTO.getLogin())).thenReturn(null);
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.of(new Usuario()));

        String mensagemEmailJaCadastrado = assertThrows(ValidationException.class,
                () -> usuarioService.validaUsuario(usuarioDTO))
                .getMessage();

        assertThat(mensagemEmailJaCadastrado).isEqualTo("E-mail já cadastrado, tente novamente!");
    }

    @Test
    void validaEmailIncorreto() {
        usuarioDTO.setEmail("aaa");
        when(usuarioRepository.findByLogin(usuarioDTO.getLogin())).thenReturn(null);
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.empty());

        String mensagemEmailJaCadastrado = assertThrows(ValidationException.class,
                () -> usuarioService.validaUsuario(usuarioDTO))
                .getMessage();

        assertThat(mensagemEmailJaCadastrado).isEqualTo("Não é um E-mail Válido!");
    }

    @Test
    void validaSenha() {
        usuarioDTO.setSenha("Aa");
        String mensagemSenhaInvalida = assertThrows(ValidationException.class,
                () -> usuarioService.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao())).getMessage();

        assertThat(mensagemSenhaInvalida)
                .isEqualTo("Senha deve conter entre 6 e 150 caracteres sendo ao menos 1 letra maiúscula, 1 minúscula e 1 número!");
    }

    @Test
    void validaSenhaDiferenteConfirmacao() {
        usuarioDTO.setSenha("Aaaaa1");
        String mensagemSenhaDiferenteConfirmacao = assertThrows(ValidationException.class,
                () -> usuarioService.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao())).getMessage();

        assertThat(mensagemSenhaDiferenteConfirmacao)
                .isEqualTo("Senha e Senha de Confirmação não Conferem, tente novamente!");
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

        when(emprestimoRepository.existsByUsuarioId(usuarioId)).thenReturn(false);
        usuarioService.deleteById(usuarioId);

        verify(usuarioRepository).deleteById(usuarioId);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void deleteByIdThrows() {
        when(emprestimoRepository.existsByUsuarioId(usuarioDTO.getId())).thenReturn(true);
        assertThrows(ValidationException.class, () -> usuarioService.deleteById(usuarioDTO.getId()));
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

    @Test
    void confirmResetPassword() {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setToken("1234");
        newPasswordDTO.setSenha("123Carmelito");
        newPasswordDTO.setSenhaConfirmacao("123Carmelito");

        when(tokenService.getSubject(any())).thenReturn("123");
        when(usuarioRepository.findByResetToken(any())).thenReturn(Optional.of(new Usuario()));
        when(passwordEncoder.encode(any())).thenReturn("123456");
        when(usuarioRepository.save(any())).thenReturn(new Usuario());

        usuarioService.confirmResetPassword(newPasswordDTO);

        verify(usuarioRepository).save(any(Usuario.class));
    }
    }