package com.bibliotecaelo.service;

import com.bibliotecaelo.configuration.MinioPropsConfiguration;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.repository.BucketFileRepository;
import com.bibliotecaelo.repository.LivroRepository;
import io.minio.MinioClient;
import jakarta.validation.ValidationException;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LivroService
        extends CrudBucketService<Livro> {

    @Getter
    private final LivroRepository repository;

    private final CategoriaService categoriaService;

    public LivroService(
        LivroRepository repository,
        CategoriaService categoriaService,
        MinioPropsConfiguration props,
        MinioClient client,
        BucketFileRepository bucketFileRepository) {

        super(props, client, bucketFileRepository);

        this.repository = repository;
        this.categoriaService = categoriaService;
    }

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
