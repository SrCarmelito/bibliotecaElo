package com.bibliotecaelo.resource;

import java.util.UUID;

import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.service.RecomendacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recomendacoes")
@RequiredArgsConstructor
public class RecomendacaoResource {

    private final RecomendacaoService service;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Page<LivroDTO>> recomendacoesPorUsuario(
            @PathVariable("usuarioId") UUID usuarioId) {
        return ResponseEntity.ok(service.getRecomendacoes(usuarioId).map(new LivroDTOConverter()::to));
    }
}
