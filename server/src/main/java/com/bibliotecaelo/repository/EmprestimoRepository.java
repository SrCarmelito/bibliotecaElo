package com.bibliotecaelo.repository;

import java.util.UUID;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository
        extends RsqlRepository<Emprestimo, UUID> {

    boolean existsByLivroIdAndStatus(UUID livroId, StatusEmprestimoEnum status);

    boolean existsByLivroId(UUID livroId);

    boolean existsByUsuarioId(UUID usuarioId);
}
