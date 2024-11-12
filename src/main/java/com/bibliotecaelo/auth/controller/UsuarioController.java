package com.bibliotecaelo.auth.controller;

import com.bibliotecaelo.auth.service.UsuarioService;
import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.auth.dto.NewPasswordDTO;
import com.bibliotecaelo.auth.dto.UsuarioDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO login) {
        return usuarioService.gerarToken(login);
    }

    @PostMapping("/novo-usuario")
    public ResponseEntity<Void> newUser(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        usuarioService.newUser(usuarioDTO);
        return ResponseEntity.noContent().build();
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

}
