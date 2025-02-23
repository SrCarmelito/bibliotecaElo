package com.bibliotecaelo.repository;

import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository
        extends RsqlRepository<Livro, UUID> {

    boolean existsByTitulo(String titulo);

    boolean existsByIsbn(String isbn);

    @Query("select distinct l from Livro l join Emprestimo e on e.livro.categoria = l.categoria where e.usuario.id = :usuarioId "
            + " and not exists (select e2 from Emprestimo e2 where e2.livro = l and e2.usuario.id = :usuarioId) ")
    Page<Livro> getRecomendacoes(UUID usuarioId, Pageable pageable);

}
