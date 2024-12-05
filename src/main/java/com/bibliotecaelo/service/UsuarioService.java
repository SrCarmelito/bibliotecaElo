package com.bibliotecaelo.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bibliotecaelo.auth.service.EmailService;
import com.bibliotecaelo.auth.service.TokenService;
import com.bibliotecaelo.converter.UsuarioDTOConverter;
import com.bibliotecaelo.converter.UsuarioResponseDTOConverter;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.LoginDTO;
import com.bibliotecaelo.dto.usuario.NewPasswordDTO;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.dto.usuario.UsuarioResponseDTO;
import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService
        extends CrudService<Usuario> {
    private static final int EXPIRATION_TIME_NEW_PASSWORD = 5;
    private static final int EXPIRATION_TIME_LOGIN = 120;

    @Getter
    private final UsuarioRepository repository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final UsuarioResponseDTOConverter usuarioResponseDTOConverter;
    private final UsuarioDTOConverter usuarioDTOConverter;
    private final EmprestimoRepository emprestimoRepository;

    @Override
    public void beforeUpdate(Usuario usuario) {
        validaSenha(usuario.getSenha(), usuario.getSenha());
    }

    @Override
    public void beforeDelete(UUID id) {
        if (emprestimoRepository.existsByUsuarioId(id)) {
            throw new ValidationException("Usuário Possui Empréstimo vinculado, portanto NÃO será excluído!");
        }
    }

    public UsuarioResponseDTO novoUsuario(UsuarioDTO usuarioDTO) {
        validaUsuario(usuarioDTO);
        validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao());

        Usuario novoUsuario = usuarioDTOConverter.from(usuarioDTO);

        novoUsuario.setSituacao(SituacaoUsuarioEnum.INATIVO);
        novoUsuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        return usuarioResponseDTOConverter.to(repository.save(novoUsuario));
    }

    public void resetPassword(HttpServletRequest request, String email) throws IOException {
        String userMail = email.replace("{\"email\":\"", "").replace("\"}", "");

        Usuario usuario = repository.findByEmail(userMail).orElseThrow(
                () -> new IllegalArgumentException("Não corresponde a um e-mail Cadastrado"));

        String token = tokenService.gerarToken(usuario, EXPIRATION_TIME_NEW_PASSWORD);
        usuario.setResetToken(token);
        repository.saveAndFlush(usuario);

        String html = montaHtml(token, usuario);

        emailService.enviarEmail(userMail, "Carmelito - App", html);
    }

    private String montaHtml(String token, Usuario usuario) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/new-password.html");
        String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        final String replaceHref = html.replace("href-reset-password-to-replace",
                "http://127.0.0.1:5500/usuario/confirm-new-password.html?token=" + token);
        return replaceHref.replace("usuario_to_replace", usuario.getNome());
    }

    public void confirmResetPassword(NewPasswordDTO newPasswordDTO) {
        try {
            tokenService.getSubject(newPasswordDTO.getToken());
        } catch (Exception e) {
            throw new ValidationException("Token Inválido ou expirado, tente novamente!");
        }

        Usuario usuario = repository.findByResetToken(newPasswordDTO.getToken()).orElseThrow(
                () -> new ValidationException("Token Não Encontrado, tente novamente!"));

        validaSenha(newPasswordDTO.getSenha(), newPasswordDTO.getSenhaConfirmacao());
        usuario.setSenha(passwordEncoder.encode(newPasswordDTO.getSenha()));
        usuario.setResetToken(null);
        repository.save(usuario);
    }

    public String gerarToken(LoginDTO login) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setSenha(login.getSenha());
        usuarioDTO.setLogin(login.getLogin());

        Usuario usuario = getAutentication(usuarioDTO);

        if (usuario.getSituacao().equals(SituacaoUsuarioEnum.INATIVO)) {
            throw new ValidationException("Usuário está Inativo, contate o Adminsitrador do Software!");
        }

        return tokenService.gerarToken(usuario, EXPIRATION_TIME_LOGIN);
    }

    public void validaUsuario(UsuarioDTO usuarioDTO) {
        if (repository.findByLogin(usuarioDTO.getLogin()) != null) {
            throw new ValidationException("Usuário já existe, tente novamente!");
        }

        if (repository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new ValidationException("E-mail já cadastrado, tente novamente!");
        }

        Pattern patternEmail = Pattern.compile("^(.+)@(\\S+)$");
        Matcher matcherEmail = patternEmail.matcher(usuarioDTO.getEmail());
        if (!matcherEmail.find()) {
            throw new ValidationException("Não é um E-mail Válido!");
        }
    }

    public void validaSenha(String senha, String senhaConfirmacao) {
        Pattern patternSenha = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{6,150}$");
        Matcher matcherSenha = patternSenha.matcher(senha);
        if (!matcherSenha.find()) {
            throw new ValidationException
                    ("Senha deve conter entre 6 e 150 caracteres sendo ao menos 1 letra maiúscula, 1 minúscula e 1 número!");
        }

        if (!senha.equals(senhaConfirmacao)) {
            throw new ValidationException("Senha e Senha de Confirmação não Conferem, tente novamente!");
        }
    }

    public Usuario getAutentication(UsuarioDTO login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.getLogin(), login.getSenha());

        Authentication authentication = this.authenticationManager
                .authenticate(usernamePasswordAuthenticationToken);

        return (Usuario) authentication.getPrincipal();
    }
}
