package com.bibliotecaelo.resource;

import java.util.UUID;

import com.bibliotecaelo.converter.DTOConverter;
import com.bibliotecaelo.interfaces.Entidade;
import com.bibliotecaelo.interfaces.EntidadeDTO;
import com.bibliotecaelo.service.CrudService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
public abstract class CrudResource<E extends Entidade, D extends EntidadeDTO> {

    @Autowired
    protected CrudService<E> crudService;

    @Autowired
    protected DTOConverter<E, D> dtoConverter;

    @PostMapping
    public ResponseEntity<D> create(@RequestBody @Valid D dto) {
        return ResponseEntity.ok(dtoConverter.to(
                crudService.save(
                        dtoConverter.from(dto))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(dtoConverter.to(crudService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<D>> findAll(Pageable pageable) {
        return ResponseEntity.ok(crudService.findAll(pageable).map(dtoConverter::to));
    }
    @PutMapping
    public ResponseEntity<D> update(@RequestBody @Valid D dto) {
        return ResponseEntity.ok(dtoConverter.to(
                crudService.update(
                        dtoConverter.from(dto, crudService.findById(dto.getId())))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        crudService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<D>> findByRsql(String search, Pageable pageable) {
        return ResponseEntity.ok(crudService.findByRsql(search, pageable).map(dtoConverter::to));
    }

}
