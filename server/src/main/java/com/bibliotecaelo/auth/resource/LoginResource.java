package com.bibliotecaelo.auth.resource;

import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.auth.dto.NewPasswordDTO;
import com.bibliotecaelo.auth.service.LoginService;
import com.bibliotecaelo.converter.UsuarioDTOConverter;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginResource {

    private final LoginService loginService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO login) {
        return loginService.gerarToken(login);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(HttpServletRequest request,
            @RequestBody String email,
            HttpServletResponse response) throws Exception {
        loginService.resetPassword(request, email);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/confirm-reset-password")
    public ResponseEntity<Void> confirmResetPassword(@RequestBody NewPasswordDTO newPasswordDTO) {
        loginService.confirmResetPassword(newPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("verify-token")
    public boolean verifyToken() {
        try {
            return true;
        } catch (Exception e) {
            throw new ValidationException("Token Inválido ou expirado, tente novamente!");
        }
    }

    @GetMapping("/me")
    public UsuarioDTO me() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new UsuarioDTOConverter().to(usuarioService.findById(usuario.getId()));
    }
}
