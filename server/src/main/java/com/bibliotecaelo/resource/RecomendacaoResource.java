package com.bibliotecaelo.resource;


import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.service.RecomendacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recomendacoes")
@RequiredArgsConstructor
public class RecomendacaoResource {

    private final RecomendacaoService service;

    @GetMapping
    public ResponseEntity<Page<LivroDTO>> recomendacoesPorUsuario(Pageable pageable) {

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(service.getRecomendacoes(usuario.getId(), pageable).map(new LivroDTOConverter()::to));
    }
}
