package com.bibliotecaelo.service;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.repository.LivroRepository;
import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LivroService
        extends CrudService<Livro> {

    @Getter
    private final LivroRepository repository;

    private final CategoriaService categoriaService;

    @Override
    public void beforeInsert(Livro livro) {
        if (repository.existsByTitulo(livro.getTitulo())) {
            throw new ValidationException("Já existe uma livro cadastrado com este título.");
        }

        if (repository.existsByIsbn(livro.getIsbn())) {
            throw new ValidationException("Já existe uma livro cadastrado com este ISBN.");
        }

        livro.setCategoria(categoriaService.findById(livro.getCategoria().getId()));
    }

}
