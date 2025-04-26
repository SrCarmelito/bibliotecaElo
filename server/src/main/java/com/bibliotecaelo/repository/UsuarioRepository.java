package com.bibliotecaelo.repository;

import java.util.Optional;
import java.util.UUID;

import com.bibliotecaelo.domain.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends RsqlRepository<Usuario, UUID> {

    Usuario findByLogin(String login);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByResetToken(String resetToken);
    boolean existsByLogin(String login);
}
