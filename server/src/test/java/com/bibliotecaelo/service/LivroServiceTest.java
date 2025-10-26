package com.bibliotecaelo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.fixtures.CategoriaFixtures;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.repository.LivroRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @InjectMocks
    LivroService service;

    @Mock
    CategoriaService categoriaService;

    @Mock
    LivroRepository repository;

    Livro livro = LivroFixtures.LivroOProcesso();
    LivroDTO livroDTO = LivroFixtures.LivroDTOOCortico();
    Categoria categoria = CategoriaFixtures.CategoriaPolicial();

    @Test
    void beforeSave() {
        when(categoriaService.findById(livro.getCategoria().getId())).thenReturn(categoria);

        service.beforeSave(livro);

        verify(repository).existsByTitulo(livro.getTitulo());
        verify(repository).existsByIsbn(livro.getIsbn());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void save() {
        when(categoriaService.findById(livro.getCategoria().getId())).thenReturn(categoria);
        when(repository.saveAndFlush(livro)).thenReturn(livro);

        Livro livroSaved = service.save(livro);

        assertThat(livroSaved.getId()).isNotNull();
        assertThat(livroSaved.getTitulo()).isEqualTo("O Processo");
        assertThat(livroSaved.getAutor()).isEqualTo("Franz Kakfa");
        assertThat(livroSaved.getIsbn()).isEqualTo("6982568746");
        assertThat(livroSaved.getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(livroSaved.getCategoria().getDescricao()).isEqualTo("Policial");

        verify(repository).existsByTitulo(livro.getTitulo());
        verify(repository).existsByIsbn(livro.getIsbn());
        verify(repository).saveAndFlush(livro);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void saveThrowsExistsByTituloTrue() {
        when(repository.existsByTitulo(livro.getTitulo())).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.save(livro));
    }

    @Test
    void saveThrowsExistsByIsbnTrue() {
        when(repository.existsByIsbn(livro.getIsbn())).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.save(livro));
    }

    @Test
    void findById() {
        when(repository.findById(livro.getId())).thenReturn(Optional.of(livro));

        Livro livroFindById = service.findById(livro.getId());

        assertThat(livroFindById.getId()).isNotNull();
        assertThat(livroFindById.getTitulo()).isEqualTo("O Processo");
        assertThat(livroFindById.getAutor()).isEqualTo("Franz Kakfa");
        assertThat(livroFindById.getIsbn()).isEqualTo("6982568746");
        assertThat(livroFindById.getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(livroFindById.getCategoria().getDescricao()).isEqualTo("Policial");

        verify(repository).findById(livro.getId());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findByIdThrows() {
        assertThrows(EntityNotFoundException.class, () -> service.findById(livroDTO.getId()));
    }

    @Test
    void findByRsql() {
        Page<Livro> pageToReturn = new PageImpl<>(List.of(livro));
        String search = "titulo=ilike=processo";
        Pageable pageable = Pageable.ofSize(20);

        when(repository.findByRsql(search, pageable)).thenReturn(pageToReturn);

        Page<Livro> result = service.findByRsql(search, pageable);

        assertThat(result).extracting(Livro::getId).containsOnlyOnce(
                UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        assertThat(result).extracting(Livro::getTitulo).containsOnlyOnce("O Processo");
        assertThat(result).extracting(Livro::getAutor).containsOnlyOnce("Franz Kakfa");
        assertThat(result).extracting(Livro::getIsbn).containsOnlyOnce("6982568746");
        assertThat(result).extracting(Livro::getDataPublicacao).containsOnlyOnce(LocalDate.of(2010, 5, 17));
        assertThat(result).extracting(l -> l.getCategoria().getId()).containsOnlyOnce(
                UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"));
        assertThat(result).extracting(l -> l.getCategoria().getDescricao()).containsOnlyOnce("Policial");

        verify(repository, times(1)).findByRsql(search, pageable);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update() {
        Livro livroToUptate = livro;
        livroToUptate.setIsbn("987654321");
        livroToUptate.setTitulo("Titulo Modificado");
        Categoria categoriaUpdate = new Categoria();
        categoriaUpdate.setDescricao("Categoria Update");
        livroToUptate.setCategoria(categoriaUpdate);

        when(repository.saveAndFlush(livro)).thenReturn(livroToUptate);

        Livro livroUpdated = service.update(livroToUptate);

        assertThat(livroUpdated.getId()).isEqualTo(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        assertThat(livroUpdated.getIsbn()).isEqualTo("987654321");
        assertThat(livroUpdated.getTitulo()).isEqualTo("Titulo Modificado");
        assertThat(livroUpdated.getCategoria().getDescricao()).isEqualTo("Categoria Update");

        verify(repository).saveAndFlush(livroToUptate);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById() {
        when(repository.findById(UUID.fromString("e7053b9c-d057-4b8f-9571-9459b0c78d50"))).thenReturn(Optional.ofNullable(livro));

        service.deleteById(UUID.fromString("e7053b9c-d057-4b8f-9571-9459b0c78d50"));

        verify(repository).deleteById(UUID.fromString("e7053b9c-d057-4b8f-9571-9459b0c78d50"));
        verifyNoMoreInteractions(repository);
    }

}