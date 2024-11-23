package com.bibliotecaelo.repository;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.enums.CategoriaLivroEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID> {

    List<Livro> findAllByTitulo(String titulo);

    @Query(value = "select e.livro from Emprestimo e join Usuario u on u.id = e.usuario.id where u.id = :usuarioId")
    List<Livro> livrosEmprestadosPorUsuarioId(UUID usuarioId);

    List<Livro> findAllByCategoria(CategoriaLivroEnum categoria);

    List<Livro> findAllByIsbn(Long isbn);
}
