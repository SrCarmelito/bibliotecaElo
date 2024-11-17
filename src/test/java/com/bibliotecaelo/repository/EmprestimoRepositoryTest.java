package com.bibliotecaelo.repository;

import java.util.UUID;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {
    "/sql/usuario.sql", "/sql/livro.sql", "/sql/emprestimo.sql"
})
class EmprestimoRepositoryTest extends DefaultTest {

    @Autowired
    EmprestimoRepository repository;

    @Test
    void findAllByLivroIdAndStatus() {
        assertThat(repository.findAllByLivroIdAndStatus(UUID.fromString("9d707fa8-ce8b-4ec9-8b6d-5e235386a3da"),
                StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO)).hasSize(1);

        assertThat(repository.findAllByLivroIdAndStatus(UUID.fromString("9d707fa8-ce8b-4ec9-8b6d-5e235386a3da"),
                StatusEmprestimoEnum.CONCLUIDO)).hasSize(0);
    }

    @Test
    void existsByLivroId() {
        assertThat(repository.existsByLivroId(UUID.fromString("9d707fa8-ce8b-4ec9-8b6d-5e235386a3da"))).isTrue();
        assertThat(repository.existsByLivroId(UUID.fromString("fac0d069-15c6-4db9-9bd6-9783ede07986"))).isFalse();
    }

    @Test
    void existsByUsuarioId() {
        assertThat(repository.existsByUsuarioId(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"))).isTrue();
        assertThat(repository.existsByUsuarioId(UUID.fromString("fac0d069-15c6-4db9-9bd6-9783ede07986"))).isFalse();
    }
}
