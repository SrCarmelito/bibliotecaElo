package com.bibliotecaelo.repository;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    List<Emprestimo> findAllByLivroIdAndStatus(@Param("livroId") UUID livroId, @Param("status") StatusEmprestimoEnum status);

    boolean existsByLivroId(@Param("livroId") UUID livroId);
    boolean existsByUsuarioId(@Param("usuarioId") UUID usuarioId);
}
