package com.bibliotecaelo.audit.repository;

import com.bibliotecaelo.audit.domain.Revision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {
}
