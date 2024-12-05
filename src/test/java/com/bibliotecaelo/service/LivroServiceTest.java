package com.bibliotecaelo.service;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @InjectMocks
    LivroService service;

    @Mock
    LivroRepository repository;

    @Mock
    EmprestimoRepository emprestimoRepository;

    LivroDTO livroDTO = LivroFixtures.LivroDTOOCortico();

    @Test
    void beforeSave() {
        Livro livro = LivroFixtures.LivroOProcesso();

        when(repository.existsByTitulo(livro.getTitulo())).thenReturn(false);
        when(repository.existsByIsbn(livro.getIsbn())).thenReturn(false);

        service.beforeSave(livro);

        verify(repository).existsByTitulo(livro.getTitulo());
        verify(repository).existsByIsbn(livro.getIsbn());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void save() {
        Livro livro = LivroFixtures.LivroOProcesso();

        when(repository.existsByTitulo(livro.getTitulo())).thenReturn(false);
        when(repository.existsByIsbn(livro.getIsbn())).thenReturn(false);

        service.save(livro);

        verify(repository).existsByTitulo(livro.getTitulo());
        verify(repository).existsByIsbn(livro.getIsbn());
        verify(repository).saveAndFlush(livro);
        verifyNoMoreInteractions(repository);
    }

 /*   @Test
    void createThrows() {
        List<Livro> list = List.of(LivroFixtures.LivroOProcesso());
        when(repository.existsByTitulo(any())).thenReturn(list);
        assertThrows(ValidationException.class, () -> service.create(livroDTO));
    }*/

  /*  @Test
    void findById() {
        when(repository.findById(any())).thenReturn(Optional.of(new Livro()));
        when(converter.to(any())).thenReturn(livroDTO);

        LivroDTO livroFindById = converter.to(service.findById(livroDTO.getId()));

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

    @Test
    void deleteById() {
        UUID livroId = UUID.randomUUID();

        when(emprestimoRepository.existsByLivroId(livroId)).thenReturn(false);
        service.deleteById(livroId);

        verify(repository).deleteById(livroId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteByIdThrows() {
        when(emprestimoRepository.existsByLivroId(livroDTO.getId())).thenReturn(true);
        assertThrows(ValidationException.class, () -> service.deleteById(livroDTO.getId()));
    }

    @Test
    void validaISBN() {
        when(repository.existsByIsbn(any())).thenReturn(true);
        String mensagemIsbnJaCadastrado = Assertions.assertThrows(ValidationException.class,
                () -> service.save(converter.from(livroDTO))).getMessage();

        assertThat(mensagemIsbnJaCadastrado)
                .isEqualTo("Já Existe uma Livro Cadastrado com este ISBN!");
    }*/

}