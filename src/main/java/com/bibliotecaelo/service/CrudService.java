package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.interfaces.Entidade;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class CrudService<E extends Entidade> {

    public abstract JpaRepository<E, UUID> getRepository();

    public void beforeDelete(UUID id) {
    }

    public void beforeSave(E entity) {
    }

    public void beforeUpdate(E entity) {
    }

    public E save(E entity) {
        beforeSave(entity);
        return getRepository().saveAndFlush(entity);
    }

    public E findById(UUID id) {
        return getRepository().findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não foi encontrado com o id ".concat(id.toString()))
        );
    }

    public Page<E> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    public E update(E entity) {
        beforeUpdate(entity);
        return getRepository().saveAndFlush(entity);
    }

    public void deleteById(UUID id) {
        beforeDelete(id);
        getRepository().deleteById(id);
    }

}
