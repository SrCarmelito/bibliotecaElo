package com.bibliotecaelo.resource;

import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.UsuarioDTO;
import com.bibliotecaelo.dto.UsuarioResponseDTO;
import com.bibliotecaelo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource extends CrudResource<Usuario, UsuarioDTO> {

    private final UsuarioService usuarioService;

    public UsuarioResource(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/novo-usuario")
    public ResponseEntity<UsuarioResponseDTO> novoUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.novoUsuario(usuarioDTO));
    }

}
