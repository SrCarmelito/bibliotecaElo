package com.bibliotecaelo.repository;

import java.util.UUID;

import com.bibliotecaelo.domain.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {
}
