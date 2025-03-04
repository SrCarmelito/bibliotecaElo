package com.bibliotecaelo.converter;

import java.util.UUID;

import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.dto.CategoriaDTO;
import com.bibliotecaelo.fixtures.CategoriaFixtures;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoriaDTOConverterTest {

    CategoriaDTOConverter converter = new CategoriaDTOConverter();

    @Test
    void from() {
        Categoria categoria = converter.from(CategoriaFixtures.CategoriaDTORomance());

        assertThat(categoria.getId()).isEqualTo(UUID.fromString("fc5944ed-84ea-40c1-9644-2b17ecb45eec"));
        assertThat(categoria.getDescricao()).isEqualTo("Romance");
    }

    @Test
    void to() {
        CategoriaDTO categoriaDTO = converter.to(CategoriaFixtures.CategoriaPolicial());

        assertThat(categoriaDTO.getId()).isEqualTo(UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"));
        assertThat(categoriaDTO.getDescricao()).isEqualTo("Policial");
    }

}
