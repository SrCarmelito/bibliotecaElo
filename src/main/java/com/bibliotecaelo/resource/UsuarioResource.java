package com.bibliotecaelo.resource;

import java.util.UUID;

import com.bibliotecaelo.dto.usuario.LoginDTO;
import com.bibliotecaelo.dto.usuario.NewPasswordDTO;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.dto.usuario.UsuarioResponseDTO;
import com.bibliotecaelo.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private final UsuarioService usuarioService;

    public UsuarioResource(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/novo-usuario")
    public ResponseEntity<UsuarioResponseDTO> newUser(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.novoUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO login) {
        return usuarioService.gerarToken(login);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(HttpServletRequest request,
                                                @RequestBody String email,
                                                HttpServletResponse response) throws Exception {
        usuarioService.resetPassword(request, email);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/confirm-reset-password")
    public ResponseEntity<Void> confirmResetPassword(@RequestBody NewPasswordDTO newPasswordDTO){
        usuarioService.confirmResetPassword(newPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioResponseDTO> update(
            @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.update(usuarioDTO));
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(usuarioService.findById(usuarioId));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> deleteByUsuarioId(@PathVariable UUID usuarioId) {
        usuarioService.deleteById(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
