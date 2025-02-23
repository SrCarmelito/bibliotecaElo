package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.LivroRepository;
import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LivroService
        extends CrudService<Livro> {

    @Getter
    private final LivroRepository repository;

    private final EmprestimoRepository emprestimoRepository;

    private final CategoriaService categoriaService;

    @Override
    public void beforeSave(Livro livro) {
        if (repository.existsByTitulo(livro.getTitulo())) {
            throw new ValidationException("Já Existe uma Livro Cadastrado com este Título!");
        }

        if (repository.existsByIsbn(livro.getIsbn())) {
            throw new ValidationException("Já Existe uma Livro Cadastrado com este ISBN!");
        }

        log.info("id da categoria{}", livro.getCategoria().getId());
        log.info("categoria{}", categoriaService.findById(livro.getCategoria().getId()));

        livro.setCategoria(categoriaService.findById(livro.getCategoria().getId()));
    }

    @Override
    public void beforeDelete(UUID livroId) {
        if (emprestimoRepository.existsByLivroId(livroId)) {
            throw new ValidationException("Livro Possui Empréstimo vinculado, portanto NÃO será excluído!");
        }
    }

}
