package com.bibliotecaelo.auth.validations;

import com.bibliotecaelo.dto.UsuarioDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        usuarioDTO = UsuarioFixtures.usuarioCarmelitoDTO();
    }

    UsuarioDTO usuarioDTO;

    @Test
    void validaUsuarioExistente() {
        when(usuarioRepository.existsByLogin(usuarioDTO.getLogin())).thenReturn(true);

        String mensagemUsuarioJaExiste = assertThrows(ValidationException.class,
                () -> userValidations.validaUsuario(usuarioDTO))
                .getMessage();

        assertThat(mensagemUsuarioJaExiste).isEqualTo("Usuário já existe, tente novamente.");
    }

    @Test
    void validaEmailJaCadastrado() {
        when(usuarioRepository.existsByLogin(usuarioDTO.getLogin())).thenReturn(false);
        when(usuarioRepository.existsByEmail(usuarioDTO.getEmail())).thenReturn(true);

        String mensagemEmailJaCadastrado = assertThrows(ValidationException.class,
                () -> userValidations.validaUsuario(usuarioDTO))
                .getMessage();

        assertThat(mensagemEmailJaCadastrado).isEqualTo("E-mail já cadastrado, tente novamente.");
    }

    @Test
    void validaSenha() {
        usuarioDTO.setSenha("Aa");
        String mensagemSenhaInvalida = assertThrows(ValidationException.class,
                () -> userValidations.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao())).getMessage();

        assertThat(mensagemSenhaInvalida)
                .isEqualTo(
                        "Senha deve conter entre 6 e 15 caracteres sendo ao menos 1 caractere especial, 1 letra maiúscula, 1 minúscula e 1 número.");
    }

    @Test
    void validaSenhaDiferenteConfirmacao() {
        usuarioDTO.setSenha("PassW0!&*");
        String mensagemSenhaDiferenteConfirmacao = assertThrows(ValidationException.class,
                () -> userValidations.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao())).getMessage();

        assertThat(mensagemSenhaDiferenteConfirmacao)
                .isEqualTo("Senha e senha de confirmação não conferem, tente novamente.");
    }


}
