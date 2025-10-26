package com.bibliotecaelo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.fixtures.CategoriaFixtures;
import com.bibliotecaelo.repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @InjectMocks
    CategoriaService service;

    @Mock
    CategoriaRepository repository;

    Categoria categoria = CategoriaFixtures.CategoriaPolicial();

    @Test
    void beforeSave() {
        service.beforeSave(categoria);

        verify(repository).existsByDescricao(categoria.getDescricao());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void save() {
        when(repository.saveAndFlush(categoria)).thenReturn(categoria);

        Categoria categoriaSaved = service.save(categoria);

        assertThat(categoriaSaved.getId()).isNotNull();
        assertThat(categoriaSaved.getDescricao()).isEqualTo("Policial");

        verify(repository).existsByDescricao(categoria.getDescricao());
        verify(repository).saveAndFlush(categoria);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void saveThrowsExistsByDescricaoTrue() {
        when(repository.existsByDescricao(categoria.getDescricao())).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.save(categoria));
    }

    @Test
    void findById() {
        when(repository.findById(categoria.getId())).thenReturn(Optional.ofNullable(categoria));

        Categoria categoriaFindedById = service.findById(categoria.getId());

        assertThat(categoriaFindedById.getId()).isEqualTo(UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"));
        assertThat(categoriaFindedById.getDescricao()).isEqualTo("Policial");

        verify(repository).findById(categoria.getId());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findByIdThrows() {
        assertThrows(EntityNotFoundException.class, () -> service.findById(categoria.getId()));
    }

    @Test
    void findByRsql() {
        Page<Categoria> pageToReturn = new PageImpl<>(List.of(categoria));
        String search = "descricao=ilike=poli";
        Pageable pageable = Pageable.ofSize(20);

        when(repository.findByRsql(search, pageable)).thenReturn(pageToReturn);

        Page<Categoria> result = service.findByRsql(search, pageable);

        assertThat(result).extracting(Categoria::getId).containsOnlyOnce(UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"));
        assertThat(result).extracting(Categoria::getDescricao).containsOnlyOnce("Policial");

        verify(repository, times(1)).findByRsql(search, pageable);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update() {
        Categoria categoriaToUpdate = categoria;
        categoria.setDescricao("Descrição Atualizada");

        when(repository.saveAndFlush(categoria)).thenReturn(categoriaToUpdate);

        Categoria categoriaUpdated = service.update(categoriaToUpdate);

        assertThat(categoriaUpdated.getId()).isEqualTo(UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"));
        assertThat(categoriaUpdated.getDescricao()).isEqualTo("Descrição Atualizada");
        verify(repository).saveAndFlush(categoriaUpdated);
    }

    @Test
    void deleteById() {
        when(repository.findById(UUID.fromString("1462f305-8356-4b10-9b4e-e87f54177a96"))).thenReturn(
                Optional.ofNullable(categoria));

        service.deleteById(UUID.fromString("1462f305-8356-4b10-9b4e-e87f54177a96"));

        verify(repository).deleteById(UUID.fromString("1462f305-8356-4b10-9b4e-e87f54177a96"));
        verifyNoMoreInteractions(repository);
    }

}
