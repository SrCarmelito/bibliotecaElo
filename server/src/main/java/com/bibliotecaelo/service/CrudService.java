package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.interfaces.Entidade;
import com.bibliotecaelo.repository.RsqlRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
public abstract class CrudService<E extends Entidade> {

    @PersistenceContext
    private EntityManager entityManager;

    public abstract RsqlRepository<E, UUID> getRepository();

    public void beforeDelete(UUID id) {
    }

    public void beforeSave(E entity) {
    }

    public void beforeUpdate(E entity) {
    }

    public E save(E entity) {
        beforeSave(entity);
        E entitySaved = getRepository().saveAndFlush(entity);
        log.info("Salvando a entidade {} - {}", entity.getClass().getSimpleName(), entitySaved);
        return entitySaved;
    }

    public E findById(UUID id) {
        return getRepository().findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não foi encontrado com o id ".concat(id.toString()))
        );
    }

    public Page<E> findByRsql(String search, Pageable pageable) {
        return getRepository().findByRsql(search, pageable);
    }

    public E update(E entity) {
        beforeUpdate(entity);
        E entityUpdated = getRepository().saveAndFlush(entity);
        log.info("Atualizando a entidade {} - {}", entity.getClass().getSimpleName(), entityUpdated);
        return entityUpdated;
    }

    public void deleteById(UUID id) {
        beforeDelete(id);
        E entityDeleted = findById(id);
        getRepository().deleteById(id);
        log.info("Deletado a entidade {}", entityDeleted);
    }
}
