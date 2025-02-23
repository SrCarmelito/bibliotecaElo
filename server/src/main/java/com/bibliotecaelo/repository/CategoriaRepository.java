package com.bibliotecaelo.repository;

import java.util.UUID;

import com.bibliotecaelo.domain.Categoria;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends RsqlRepository<Categoria, UUID> {

    boolean existsByDescricao(String descricao);
}
