package com.bibliotecaelo.auth.validations;

import java.util.Optional;

import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidationsTest {

    @InjectMocks
    UserValidations userValidations;

    @Mock
    UsuarioRepository usuarioRepository;

    UsuarioDTO usuarioDTO = UsuarioFixtures.usuarioCarmelitoDTO();

    @Test
    void validaUsuarioExistente() {
        when(usuarioRepository.findByLogin(usuarioDTO.getLogin())).thenReturn(new Usuario());

        String mensagemUsuarioJaExiste = assertThrows(ValidationException.class,
                () -> userValidations.validaUsuario(usuarioDTO))
                .getMessage();

        assertThat(mensagemUsuarioJaExiste).isEqualTo("Usuário já existe, tente novamente!");
    }

    @Test
    void validaEmailJaCadastrado() {
        when(usuarioRepository.findByLogin(usuarioDTO.getLogin())).thenReturn(null);
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.of(new Usuario()));

        String mensagemEmailJaCadastrado = assertThrows(ValidationException.class,
                () -> userValidations.validaUsuario(usuarioDTO))
                .getMessage();

        assertThat(mensagemEmailJaCadastrado).isEqualTo("E-mail já cadastrado, tente novamente!");
    }

    @Test
    void validaEmailIncorreto() {
        usuarioDTO.setEmail("aaa");
        when(usuarioRepository.findByLogin(usuarioDTO.getLogin())).thenReturn(null);
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.empty());

        String mensagemEmailJaCadastrado = assertThrows(ValidationException.class,
                () -> userValidations.validaUsuario(usuarioDTO))
                .getMessage();

        assertThat(mensagemEmailJaCadastrado).isEqualTo("Não é um E-mail Válido!");
    }

    @Test
    void validaSenha() {
        usuarioDTO.setSenha("Aa");
        String mensagemSenhaInvalida = assertThrows(ValidationException.class,
                () -> userValidations.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao())).getMessage();

        assertThat(mensagemSenhaInvalida)
                .isEqualTo(
                        "Senha deve conter entre 6 e 150 caracteres sendo ao menos 1 Caractere especial, 1 letra maiúscula, 1 minúscula e 1 número!");
    }

    @Test
    void validaSenhaDiferenteConfirmacao() {
        usuarioDTO.setSenha("PassW0!&*");
        String mensagemSenhaDiferenteConfirmacao = assertThrows(ValidationException.class,
                () -> userValidations.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao())).getMessage();

        assertThat(mensagemSenhaDiferenteConfirmacao)
                .isEqualTo("Senha e Senha de Confirmação não Conferem, tente novamente!");
    }


}
