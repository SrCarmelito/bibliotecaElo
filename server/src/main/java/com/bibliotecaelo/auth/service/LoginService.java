package com.bibliotecaelo.auth.service;

import com.bibliotecaelo.auth.dto.EmailDTO;
import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.auth.dto.NewPasswordDTO;
import com.bibliotecaelo.auth.validations.UserValidations;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.UsuarioDTO;
import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsuarioRepository repository;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserValidations userValidations;

    private static final int EXPIRATION_INT_MINUTES_NEW_PASSWORD = 5;
    private static final int EXPIRATION_INT_MINUTES_LOGIN = 120;

    public void resetPassword(HttpServletRequest request, EmailDTO emailDTO) throws IOException {

        Usuario usuario = repository.findByEmail(emailDTO.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("Não corresponde a um e-mail cadastrado."));

        String token = tokenService.gerarToken(usuario, EXPIRATION_INT_MINUTES_NEW_PASSWORD);
        usuario.setResetToken(token);
        repository.saveAndFlush(usuario);

        String html = montaHtml(request.getHeader("Origin"), token, usuario.getNome());

        emailService.enviarEmail(usuario.getEmail(), "Carmelito - App", html);
    }

    private String montaHtml(String origin, String token, String userName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/new-password.html");
        String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        final String replaceHref = html.replace("href-reset-password-to-replace",
                origin + "/confirm-new-password?token=" + token);
        return replaceHref.replace("usuario_to_replace", userName);
    }

    public void confirmResetPassword(NewPasswordDTO newPasswordDTO) {
        try {
            tokenService.getSubject(newPasswordDTO.getToken());
        } catch (Exception e) {
            throw new ValidationException("Token inválido ou expirado, tente novamente.");
        }

        Usuario usuario = repository.findByResetToken(newPasswordDTO.getToken()).orElseThrow(
                () -> new ValidationException("Token não encontrado, tente novamente."));

        userValidations.validaSenha(newPasswordDTO.getSenha(), newPasswordDTO.getSenhaConfirmacao());
        usuario.setSenha(passwordEncoder.encode(newPasswordDTO.getSenha()));
        usuario.setResetToken(null);
        repository.save(usuario);
    }

    public String gerarToken(LoginDTO login) {

        if (!repository.existsByLogin(login.getLogin())) {
            throw new EntityNotFoundException("Usuário não encontrado, tente novamente.");
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setSenha(login.getSenha());
        usuarioDTO.setLogin(login.getLogin());

        Usuario usuario = getAutentication(usuarioDTO);

        if (usuario.getSituacao().equals(SituacaoUsuarioEnum.INATIVO)) {
            throw new ValidationException("Usuário está inativo, contate o administrador do software.");
        }

        return tokenService.gerarToken(usuario, EXPIRATION_INT_MINUTES_LOGIN);
    }

    public Usuario getAutentication(UsuarioDTO login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.getLogin(), login.getSenha());

        Authentication authentication = this.authenticationManager
                .authenticate(usernamePasswordAuthenticationToken);

        return (Usuario) authentication.getPrincipal();
    }

}
