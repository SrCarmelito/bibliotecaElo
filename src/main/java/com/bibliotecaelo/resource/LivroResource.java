package com.bibliotecaelo.resource;

import java.util.UUID;

import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.service.LivroService;
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
@RequestMapping("/api/livros")
public class LivroResource {

    private final LivroService service;

    public LivroResource(LivroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LivroDTO> create(@RequestBody @Valid LivroDTO livroDTO) {
        return ResponseEntity.ok(service.create(livroDTO));
    }

    @GetMapping("/{livroId}")
    public ResponseEntity<LivroDTO> findById(@PathVariable("livroId") UUID livroId) {
        return ResponseEntity.ok(service.findById(livroId));
    }

    @GetMapping
    public ResponseEntity<Page<LivroDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping
    public ResponseEntity<LivroDTO> update(@RequestBody @Valid LivroDTO livroDTO) {
        return ResponseEntity.ok(service.update(livroDTO));
    }

    @DeleteMapping("/{livroId}")
    public ResponseEntity<Void> deleteById(@PathVariable("livroId") UUID livroId) {
        service.deleteById(livroId);
        return ResponseEntity.noContent().build();
    }

}
