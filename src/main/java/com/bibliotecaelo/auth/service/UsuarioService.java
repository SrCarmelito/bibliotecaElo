package com.bibliotecaelo.auth.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bibliotecaelo.auth.converter.UsuarioDTOConverter;
import com.bibliotecaelo.auth.converter.UsuarioResponseDTOConverter;
import com.bibliotecaelo.auth.domain.Usuario;
import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.auth.dto.NewPasswordDTO;
import com.bibliotecaelo.auth.dto.UsuarioDTO;
import com.bibliotecaelo.auth.dto.UsuarioResponseDTO;
import com.bibliotecaelo.auth.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.auth.repository.UsuarioRepository;
import com.bibliotecaelo.repository.EmprestimoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private static final int EXPIRATION_TIME_NEW_PASSWORD = 5;
    private static final int EXPIRATION_TIME_LOGIN = 120;

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final UsuarioResponseDTOConverter usuarioResponseDTOConverter;
    private final UsuarioDTOConverter usuarioDTOConverter;
    private final EmprestimoRepository emprestimoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, EmailService emailService, TokenService tokenService,
            UsuarioResponseDTOConverter usuarioResponseDTOConverter, UsuarioDTOConverter usuarioDTOConverter,
            EmprestimoRepository emprestimoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.usuarioResponseDTOConverter = usuarioResponseDTOConverter;
        this.usuarioDTOConverter = usuarioDTOConverter;
        this.emprestimoRepository = emprestimoRepository;
    }

    public UsuarioResponseDTO novoUsuario(UsuarioDTO usuarioDTO) {
        validaUsuario(usuarioDTO);
        validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao());

        Usuario novoUsuario = usuarioDTOConverter.from(usuarioDTO);

        novoUsuario.setSituacao(SituacaoUsuarioEnum.INATIVO);
        novoUsuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        return usuarioResponseDTOConverter.to(usuarioRepository.save(novoUsuario));
    }

    public void resetPassword(HttpServletRequest request, String email) throws IOException {
        String userMail = email.replace("{\"email\":\"", "").replace("\"}", "");

        Usuario usuario = usuarioRepository.findByEmail(userMail).orElseThrow(
                () -> new IllegalArgumentException("Não corresponde a um e-mail Cadastrado"));

        String token = tokenService.gerarToken(usuario, EXPIRATION_TIME_NEW_PASSWORD);
        usuario.setResetToken(token);
        usuarioRepository.saveAndFlush(usuario);

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

        Usuario usuario = usuarioRepository.findByResetToken(newPasswordDTO.getToken()).orElseThrow(
                () -> new ValidationException("Token Não Encontrado, tente novamente!"));

        validaSenha(newPasswordDTO.getSenha(), newPasswordDTO.getSenhaConfirmacao());
        usuario.setSenha(passwordEncoder.encode(newPasswordDTO.getSenha()));
        usuario.setResetToken(null);
        usuarioRepository.save(usuario);
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
        Pattern patternSenha = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{6,150}$");
        Matcher matcherSenha = patternSenha.matcher(senha);
        if (!matcherSenha.find()) {
            throw new ValidationException
                    ("Senha deve conter entre 6 e 150 caracteres sendo ao menos 1 letra maiúscula, 1 minúscula e 1 número!");
        }

        if (!senha.equals(senhaConfirmacao)){
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

    public UsuarioResponseDTO findById(UUID usuarioId) {
        return usuarioResponseDTOConverter.to(usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário Não Encontrado")));
    }

    public UsuarioResponseDTO update(UsuarioDTO usuarioDTO) {

        final Usuario usuarioToUpdate = usuarioRepository.findById(usuarioDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário Não Encontrado"));

        usuarioDTOConverter.from(usuarioDTO, usuarioToUpdate);

        usuarioRepository.saveAndFlush(usuarioToUpdate);

        return new UsuarioResponseDTOConverter().to(usuarioToUpdate);
    }

    public void deleteById(UUID usuarioId) {
        if (emprestimoRepository.existsByUsuarioId(usuarioId)) {
            throw new ValidationException("Usuário Possui Empréstimo vinculado, portanto NÃO será excluído!");
        }

        usuarioRepository.deleteById(usuarioId);
    }

    public Page<UsuarioResponseDTO> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(usuarioResponseDTOConverter::to);
    }
}
