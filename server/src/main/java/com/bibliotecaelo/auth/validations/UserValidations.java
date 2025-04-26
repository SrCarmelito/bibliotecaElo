package com.bibliotecaelo.auth.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidations {

    private final UsuarioRepository usuarioRepository;

    public void validaUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByLogin(usuarioDTO.getLogin()) != null) {
            throw new ValidationException("Usuário já existe, tente novamente!");
        }

        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new ValidationException("E-mail já cadastrado, tente novamente!");
        }

        Pattern patternEmail = Pattern.compile("^(.+)@(\\S+)$");
        Matcher matcherEmail = patternEmail.matcher(usuarioDTO.getEmail());
        if (!matcherEmail.find()) {
            throw new ValidationException("Não é um E-mail Válido!");
        }
    }

    public void validaSenha(String senha, String senhaConfirmacao) {
        Pattern patternSenha = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W).{6,150}$");
        Matcher matcherSenha = patternSenha.matcher(senha);
        if (!matcherSenha.find()) {
            throw new ValidationException
                    ("Senha deve conter entre 6 e 150 caracteres sendo ao menos 1 letra maiúscula, 1 minúscula e 1 número!");
        }

        if (!senha.equals(senhaConfirmacao)) {
            throw new ValidationException("Senha e Senha de Confirmação não Conferem, tente novamente!");
        }
    }
}
