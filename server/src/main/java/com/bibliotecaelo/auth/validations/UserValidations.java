package com.bibliotecaelo.auth.validations;

import com.bibliotecaelo.dto.UsuarioDTO;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidations {

    private final UsuarioRepository usuarioRepository;

    public void validaUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByLogin(usuarioDTO.getLogin())) {
            throw new ValidationException("Usuário já existe, tente novamente.");
        }

        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new ValidationException("E-mail já cadastrado, tente novamente.");
        }
    }

    public void validaSenha(String senha, String senhaConfirmacao) {
        Pattern patternSenha = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[^\\w\\s]).{6,15}$");
        Matcher matcherSenha = patternSenha.matcher(senha);
        if (!matcherSenha.find()) {
            throw new ValidationException
                    ("Senha deve conter entre 6 e 15 caracteres sendo ao menos 1 caractere especial, 1 letra maiúscula, 1 minúscula e 1 número.");
        }

        if (!senha.equals(senhaConfirmacao)) {
            throw new ValidationException("Senha e senha de confirmação não conferem, tente novamente.");
        }
    }
}
