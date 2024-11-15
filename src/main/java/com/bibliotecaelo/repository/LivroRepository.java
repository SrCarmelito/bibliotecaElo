package com.bibliotecaelo.repository;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID> {

    List<Livro> findByTitulo(String titulo);
}
