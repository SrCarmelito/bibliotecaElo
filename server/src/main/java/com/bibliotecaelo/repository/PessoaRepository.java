package com.bibliotecaelo.repository;

import java.util.UUID;

import com.bibliotecaelo.domain.Pessoa;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends RsqlRepository<Pessoa, UUID>{

    void deleteByIdIntegration(Long idIntegration);
}
