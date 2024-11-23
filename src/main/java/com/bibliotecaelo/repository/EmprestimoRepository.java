package com.bibliotecaelo.repository;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    List<Emprestimo> findAllByLivroIdAndStatus(UUID livroId, StatusEmprestimoEnum status);

    boolean existsByLivroId(UUID livroId);
    boolean existsByUsuarioId(UUID usuarioId);
}
