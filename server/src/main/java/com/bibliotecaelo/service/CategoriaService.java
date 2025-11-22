package com.bibliotecaelo.service;

import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.repository.CategoriaRepository;
import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaService
        extends CrudService<Categoria> {

    @Getter
    private final CategoriaRepository repository;

    private static final String descricaoRepetidaMessage = "Já existe uma categoria cadastrada com esta descrição.";

    @Override
    public void beforeInsert(Categoria categoria) {
        if (repository.existsByDescricao(categoria.getDescricao())) {
            throw new ValidationException(descricaoRepetidaMessage);
        }
    }

    @Override
    public void beforeUpdate(Categoria entity) {
        if(repository.existsByDescricaoAndIdNot(entity.getDescricao(), entity.getId())) {
            throw new ValidationException(descricaoRepetidaMessage);
        }
    }
}
