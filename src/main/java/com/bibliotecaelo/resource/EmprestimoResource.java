package com.bibliotecaelo.resource;

import java.util.UUID;

import com.bibliotecaelo.dto.Devolucao;
import com.bibliotecaelo.dto.EmprestimoDTO;
import com.bibliotecaelo.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoResource {

    private final EmprestimoService service;

    public EmprestimoResource(EmprestimoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EmprestimoDTO> create(@RequestBody @Valid EmprestimoDTO emprestimoDTO) {
        return ResponseEntity.ok(service.create(emprestimoDTO));
    }

    @PutMapping
    public ResponseEntity<EmprestimoDTO> update(@RequestBody @Valid Devolucao devolucao) {
        return ResponseEntity.ok(service.update(devolucao));
    }

    @GetMapping("/{emprestimoId}")
    public ResponseEntity<EmprestimoDTO> findById(@PathVariable("emprestimoId")UUID emprestimoId) {
        return ResponseEntity.ok(service.findById(emprestimoId));
    }

    @GetMapping
    public ResponseEntity<Page<EmprestimoDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }
}
