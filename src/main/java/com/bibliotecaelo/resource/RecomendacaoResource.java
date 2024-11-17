package com.bibliotecaelo.resource;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.service.RecomendacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacaoResource {

    private final RecomendacaoService service;

    public RecomendacaoResource(RecomendacaoService service) {
        this.service = service;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<LivroDTO>> recomendacoesPorUsuario(
            @PathVariable("usuarioId")UUID usuarioId) {
        return ResponseEntity.ok(service.getRecomendacoes(usuarioId));
    }
}
