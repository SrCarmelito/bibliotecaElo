package com.bibliotecaelo.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.enums.CategoriaLivroEnum;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class LivroServiceTest extends DefaultTest {

    @InjectMocks
    LivroService service;

    @Mock
    LivroRepository repository;

    @Mock
    LivroDTOConverter converter;

    @Mock
    Pageable pageable;

    LivroDTO livroDTO = LivroFixtures.LivroDTOOCortico();

    @Test
    void create() {
        Livro livro = converter.from(livroDTO);
        when(converter.from(livroDTO)).thenReturn(livro);
        when(repository.save(any())).thenReturn(livro);

        service.create(livroDTO);

        verify(repository).findByTitulo(livroDTO.getTitulo());
        verify(repository).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createThrows() {
        List<Livro> list = List.of(LivroFixtures.LivroOProcesso());
        when(repository.findByTitulo(any())).thenReturn(list);
        assertThrows(ValidationException.class, () -> service.create(livroDTO));
    }

    @Test
    void findById() {
        when(repository.findById(any())).thenReturn(Optional.of(new Livro()));
        when(converter.to(any())).thenReturn(livroDTO);

        LivroDTO livroFindById = service.findById(livroDTO.getId());

        assertThat(livroFindById.getTitulo()).isEqualTo("O cortiço");
        assertThat(livroFindById.getCategoria()).isEqualTo(CategoriaLivroEnum.FICCAO_CIENTIFICA);
        assertThat(livroFindById.getAutor()).isEqualTo("Aluísio Azevedo");

        verify(repository).findById(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findByIdThrows() {
        assertThrows(EntityNotFoundException.class, () -> service.findById(livroDTO.getId()));
    }

    @Test
    void findAll() {
        Page<Livro> page = Page.empty();
        Pageable pageable = Pageable.unpaged();

        when(repository.findAll(pageable)).thenReturn(page);

        service.findAll(pageable);

        verify(repository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update() {
        Livro livro = converter.from(livroDTO);

        when(repository.findById(livroDTO.getId())).thenReturn(Optional.of(new Livro()));
        when(repository.saveAndFlush(any())).thenReturn(livro);

        service.update(livroDTO);

        verify(repository).findById(livroDTO.getId());
        verify(repository).saveAndFlush(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateThrows() {
        assertThrows(EntityNotFoundException.class, () -> service.update(livroDTO));
    }

/*    @Test
    void deleteById() {
        // TODO validar se o LIVRO tem registro de empréstimo, se tiver, não pode deixar deletar
    }*/
}