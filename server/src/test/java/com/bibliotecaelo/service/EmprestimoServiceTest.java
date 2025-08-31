package com.bibliotecaelo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.fixtures.EmprestimoFixtures;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.LivroRepository;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @InjectMocks
    EmprestimoService service;

    @Mock
    EmprestimoRepository emprestimoRepository;

    @Mock
    LivroRepository livroRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    Emprestimo emprestimo = EmprestimoFixtures.EmprestimoValido();
    Usuario usuario = UsuarioFixtures.usuarioPele();
    Livro livro = LivroFixtures.LivroOProcesso();

    @Test
    void beforeSave() {
        when(usuarioRepository.findById(emprestimo.getUsuario().getId())).thenReturn(Optional.ofNullable(usuario));
        when(livroRepository.findById(emprestimo.getLivro().getId())).thenReturn(Optional.ofNullable(livro));

        service.beforeSave(emprestimo);

        verify(emprestimoRepository).existsByLivroIdAndStatus(emprestimo.getLivro().getId(), StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);
        verify(usuarioRepository).findById(emprestimo.getUsuario().getId());
        verify(livroRepository).findById(emprestimo.getLivro().getId());
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoMoreInteractions(livroRepository);
    }

    @Test
    void beforeSaveExistsByLivroIdAndStatusTRUE() {
        when(emprestimoRepository.existsByLivroIdAndStatus(
                emprestimo.getLivro().getId(),
                StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO)).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.save(emprestimo));
    }

    @Test
    void validaDataEmprestimoPosteriorDevolucao() {
        emprestimo.setDataEmprestimo(emprestimo.getDataDevolucao().plusDays(1L));

        String errorMessage = assertThrows(ValidationException.class,
                () -> service.validaDataEmprestimoPosteriorDevolucao(
                        emprestimo.getDataEmprestimo(),
                        emprestimo.getDataDevolucao()))
        .getMessage();

        assertThat(errorMessage).isEqualTo("Data da Devolução menor que a data do Empréstimo, verifique!");
    }

    @Test
    void validaDataEmprestimoPosteriorDevolucaoNoMesmoDia() {
        emprestimo.setDataEmprestimo(emprestimo.getDataDevolucao());

        assertDoesNotThrow(() -> service.validaDataEmprestimoPosteriorDevolucao(
                        emprestimo.getDataEmprestimo(),
                        emprestimo.getDataDevolucao()));
    }

    @Test
    void save() {
        when(usuarioRepository.findById(emprestimo.getUsuario().getId())).thenReturn(Optional.ofNullable(usuario));
        when(livroRepository.findById(emprestimo.getLivro().getId())).thenReturn(Optional.ofNullable(livro));
        when(emprestimoRepository.saveAndFlush(emprestimo)).thenReturn(emprestimo);

        Emprestimo emprestimoSaved = service.save(emprestimo);

        assertThat(emprestimoSaved.getId()).isNotNull();
        assertThat(emprestimoSaved.getDataEmprestimo()).isEqualTo(LocalDate.of(2021, 12, 8));
        assertThat(emprestimoSaved.getDataDevolucao()).isEqualTo(LocalDate.of(2024, 8, 7));
        assertThat(emprestimoSaved.getStatus()).isEqualTo(StatusEmprestimoEnum.CONCLUIDO);

        assertThat(emprestimoSaved.getUsuario().getId()).isEqualTo(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"));
        assertThat(emprestimoSaved.getUsuario().getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(emprestimoSaved.getUsuario().getLogin()).isEqualTo("pele");

        assertThat(emprestimoSaved.getLivro().getId()).isEqualTo(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        assertThat(emprestimoSaved.getLivro().getTitulo()).isEqualTo("O Processo");
        assertThat(emprestimoSaved.getLivro().getAutor()).isEqualTo("Franz Kakfa");
        assertThat(emprestimoSaved.getLivro().getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(emprestimoSaved.getLivro().getCategoria().getDescricao()).isEqualTo("Policial");

        verify(emprestimoRepository).existsByLivroIdAndStatus(emprestimo.getLivro().getId(), StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);
        verify(usuarioRepository).findById(emprestimo.getUsuario().getId());
        verify(livroRepository).findById(emprestimo.getLivro().getId());
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoMoreInteractions(livroRepository);
    }

    @Test
    void findById() {
        when(emprestimoRepository.findById(emprestimo.getId())).thenReturn(Optional.of(emprestimo));

        Emprestimo emprestimoFindById = service.findById(emprestimo.getId());

        assertThat(emprestimoFindById.getId()).isEqualTo(UUID.fromString("2cd3f08f-60c7-4214-ac82-b89550ed8992"));
        assertThat(emprestimoFindById.getDataEmprestimo()).isEqualTo(LocalDate.of(2021, 12, 8));
        assertThat(emprestimoFindById.getDataDevolucao()).isEqualTo(LocalDate.of(2024, 8, 7));
        assertThat(emprestimoFindById.getStatus()).isEqualTo(StatusEmprestimoEnum.CONCLUIDO);

        assertThat(emprestimoFindById.getUsuario().getId()).isEqualTo(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"));
        assertThat(emprestimoFindById.getUsuario().getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(emprestimoFindById.getUsuario().getLogin()).isEqualTo("pele");

        assertThat(emprestimoFindById.getLivro().getId()).isEqualTo(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        assertThat(emprestimoFindById.getLivro().getTitulo()).isEqualTo("O Processo");
        assertThat(emprestimoFindById.getLivro().getAutor()).isEqualTo("Franz Kakfa");
        assertThat(emprestimoFindById.getLivro().getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(emprestimoFindById.getLivro().getCategoria().getDescricao()).isEqualTo("Policial");

        verify(emprestimoRepository).findById(emprestimo.getId());
        verifyNoMoreInteractions(livroRepository);
    }

    @Test
    void findByIdThrows() {
        assertThrows(EntityNotFoundException.class, () -> service.findById(emprestimo.getId()));
    }

    @Test
    void findByRsql() {
        Page<Emprestimo> pageToReturn = new PageImpl<>(List.of(emprestimo));
        String search = "livro.titulo=ilike=processo";
        Pageable pageable = Pageable.ofSize(20);

        when(emprestimoRepository.findByRsql(search, pageable)).thenReturn(pageToReturn);

        Page<Emprestimo> result = service.findByRsql(search, pageable);

        assertThat(result).extracting(Emprestimo::getId).containsOnlyOnce(UUID.fromString("2cd3f08f-60c7-4214-ac82-b89550ed8992"));
        assertThat(result).extracting(Emprestimo::getDataEmprestimo).containsOnlyOnce(LocalDate.of(2021, 12, 8));
        assertThat(result).extracting(Emprestimo::getDataDevolucao).containsOnlyOnce(LocalDate.of(2024, 8, 7));
        assertThat(result).extracting(Emprestimo::getStatus).containsOnlyOnce(StatusEmprestimoEnum.CONCLUIDO);

        assertThat(result).extracting(r -> r.getUsuario().getId()).containsOnlyOnce(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"));
        assertThat(result).extracting(r -> r.getUsuario().getNome()).containsOnlyOnce("Edson Arantes do Nascimento");
        assertThat(result).extracting(r -> r.getUsuario().getLogin()).containsOnlyOnce("pele");

        assertThat(result).extracting(r -> r.getLivro().getId()).containsOnlyOnce(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        assertThat(result).extracting(r -> r.getLivro().getTitulo()).containsOnlyOnce("O Processo");
        assertThat(result).extracting(r -> r.getLivro().getAutor()).containsOnlyOnce("Franz Kakfa");
        assertThat(result).extracting(r -> r.getLivro().getDataPublicacao()).containsOnlyOnce(LocalDate.of(2010, 5, 17));
        assertThat(result).extracting(r -> r.getLivro().getCategoria().getDescricao()).containsOnlyOnce("Policial");

        verify(emprestimoRepository).findByRsql(search, pageable);
        verifyNoMoreInteractions(livroRepository);
    }

    @Test
    void update() {
        Emprestimo emprestimToUpdate = emprestimo;
        emprestimToUpdate.setDataDevolucao(LocalDate.of(2025, 03, 19));
        emprestimToUpdate.setStatus(StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);

        when(emprestimoRepository.saveAndFlush(emprestimToUpdate)).thenReturn(emprestimToUpdate);

        Emprestimo emprestimoUpdated = service.update(emprestimToUpdate);

        assertThat(emprestimoUpdated.getId()).isEqualTo(UUID.fromString("2cd3f08f-60c7-4214-ac82-b89550ed8992"));
        assertThat(emprestimoUpdated.getDataDevolucao()).isEqualTo(LocalDate.of(2025, 03, 19));
        assertThat(emprestimoUpdated.getStatus()).isEqualTo(StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);

        assertThat(emprestimoUpdated.getUsuario().getId()).isEqualTo(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"));
        assertThat(emprestimoUpdated.getUsuario().getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(emprestimoUpdated.getUsuario().getLogin()).isEqualTo("pele");

        assertThat(emprestimoUpdated.getLivro().getId()).isEqualTo(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        assertThat(emprestimoUpdated.getLivro().getTitulo()).isEqualTo("O Processo");
        assertThat(emprestimoUpdated.getLivro().getAutor()).isEqualTo("Franz Kakfa");
        assertThat(emprestimoUpdated.getLivro().getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(emprestimoUpdated.getLivro().getCategoria().getDescricao()).isEqualTo("Policial");

        verify(emprestimoRepository).saveAndFlush(emprestimo);
        verifyNoMoreInteractions(emprestimoRepository);
    }

    @Test
    void deleteById() {
        assertThrows(IllegalStateException.class,
                () -> service.deleteById(UUID.fromString("9c1c8796-6a91-41d3-a412-bc96d1b0ab6f")));
    }

}