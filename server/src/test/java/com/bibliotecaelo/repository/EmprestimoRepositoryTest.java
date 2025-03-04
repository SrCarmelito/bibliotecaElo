package com.bibliotecaelo.repository;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.fixtures.EmprestimoFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(value = "test")
@Sql(scripts = {
        "/sql/usuario.sql", "/sql/categoria.sql",
        "/sql/livro.sql", "/sql/emprestimo.sql"
})
class EmprestimoRepositoryTest {

    @Autowired
    EmprestimoRepository repository;

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    void save() {
        Emprestimo emprestimo = EmprestimoFixtures.EmprestimoValido();
        emprestimo.setUsuario(usuarioRepository.findById(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34")).orElseThrow());
        emprestimo.setLivro(livroRepository.findById(UUID.fromString("8bf07126-eaa2-4207-b3de-cbc7a43e038f")).orElseThrow());

        Emprestimo emprestimoSalvo = repository.saveAndFlush(emprestimo);

        assertThat(emprestimoSalvo.getUsuario().getId()).isEqualTo(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"));
        assertThat(emprestimoSalvo.getUsuario().getNome()).isEqualTo("Ozzy Osbourne");
        assertThat(emprestimoSalvo.getUsuario().getDataCadastro()).isEqualTo(LocalDate.of(1970, 7, 2));

        assertThat(emprestimoSalvo.getLivro().getId()).isEqualTo(UUID.fromString("8bf07126-eaa2-4207-b3de-cbc7a43e038f"));
        assertThat(emprestimoSalvo.getLivro().getTitulo()).isEqualTo("As areias do Tempo");
        assertThat(emprestimoSalvo.getLivro().getIsbn()).isEqualTo("4475598957534");
        assertThat(emprestimoSalvo.getLivro().getCategoria().getId()).isEqualTo(UUID.fromString("51f797f6-23f3-4482-8423-cc7a06004486"));
        assertThat(emprestimoSalvo.getLivro().getCategoria().getDescricao()).isEqualTo("Humor");

        assertThat(emprestimoSalvo.getDataEmprestimo()).isEqualTo(LocalDate.of(2021, 12, 8));
        assertThat(emprestimoSalvo.getDataDevolucao()).isEqualTo(LocalDate.of(2024, 8, 7));
        assertThat(emprestimoSalvo.getStatus()).isEqualTo(StatusEmprestimoEnum.CONCLUIDO);
    }

    @Test
    @WithMockUser
    void update() {
        Emprestimo emprestimoToUpdate = repository.findById(UUID.fromString("c15102a7-5b91-4d6b-8a12-8830bd21168b")).orElseThrow();

        emprestimoToUpdate.setLivro(livroRepository.findById(UUID.fromString("9d707fa8-ce8b-4ec9-8b6d-5e235386a3da")).orElseThrow());
        emprestimoToUpdate.setDataDevolucao(LocalDate.of(2025, 2, 19));
        emprestimoToUpdate.setStatus(StatusEmprestimoEnum.CONCLUIDO);

        Emprestimo emprestimoUpdated = repository.saveAndFlush(emprestimoToUpdate);

        assertThat(emprestimoUpdated.getLivro().getId()).isEqualTo(UUID.fromString("9d707fa8-ce8b-4ec9-8b6d-5e235386a3da"));
        assertThat(emprestimoUpdated.getLivro().getTitulo()).isEqualTo("A amiga genial");
        assertThat(emprestimoUpdated.getLivro().getAutor()).isEqualTo("Elena Ferrante");
        assertThat(emprestimoUpdated.getLivro().getCategoria().getDescricao()).isEqualTo("Humor");

        assertThat(emprestimoUpdated.getDataDevolucao()).isEqualTo(LocalDate.of(2025, 2, 19));
        assertThat(emprestimoUpdated.getStatus()).isEqualTo(StatusEmprestimoEnum.CONCLUIDO);
    }

    @Test
    void deleteById() {
        repository.deleteById(UUID.fromString("c15102a7-5b91-4d6b-8a12-8830bd21168b"));

        assertThat(repository.findAll()).extracting(Emprestimo::getId)
                .doesNotContain(UUID.fromString("c15102a7-5b91-4d6b-8a12-8830bd21168b"));
    }

    @Test
    void findById() {
        Emprestimo emprestimoFinded = repository.findById(UUID.fromString("c15102a7-5b91-4d6b-8a12-8830bd21168b")).orElseThrow();

        assertThat(emprestimoFinded.getId()).isEqualTo(UUID.fromString("c15102a7-5b91-4d6b-8a12-8830bd21168b"));
        assertThat(emprestimoFinded.getStatus()).isEqualTo(StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);
        assertThat(emprestimoFinded.getDataEmprestimo()).isEqualTo(LocalDate.of(2024, 7, 6));

        assertThat(emprestimoFinded.getLivro().getId()).isEqualTo(UUID.fromString("de1c8bd9-755d-4f02-9c9b-781c25674109"));
        assertThat(emprestimoFinded.getLivro().getTitulo()).isEqualTo("Jane Eyre");
        assertThat(emprestimoFinded.getLivro().getDataPublicacao()).isEqualTo(LocalDate.of(2006, 12, 25));

        assertThat(emprestimoFinded.getUsuario().getId()).isEqualTo(UUID.fromString("5bc26f63-fc13-4e4f-8fc3-524b223a7d34"));
        assertThat(emprestimoFinded.getUsuario().getEmail()).isEqualTo("ozzy.osbourne@gmail.com");
        assertThat(emprestimoFinded.getUsuario().getLogin()).isEqualTo("ozzy");
    }

    @Test
    void findAllByLivroIdAndStatus() {
        assertThat(repository.existsByLivroIdAndStatus(UUID.fromString("9d707fa8-ce8b-4ec9-8b6d-5e235386a3da"),
                StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO)).isTrue();

        assertThat(repository.existsByLivroIdAndStatus(UUID.fromString("9d707fa8-ce8b-4ec9-8b6d-5e235386a3da"),
                StatusEmprestimoEnum.CONCLUIDO)).isFalse();
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
