package com.bibliotecaelo.converter;

import java.time.LocalDate;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.enums.CategoriaLivroEnum;
import com.bibliotecaelo.fixtures.LivroFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class LivroDTOConverterTest extends DefaultTest {

    @Autowired
    LivroDTOConverter livroDTOConverter;

    @Test
    void from() {
        Livro livro = livroDTOConverter.from(LivroFixtures.LivroDTOOCortico());

        assertThat(livro.getTitulo()).isEqualTo("O cortiço");
        assertThat(livro.getCategoria()).isEqualTo(CategoriaLivroEnum.FICCAO_CIENTIFICA);
        assertThat(livro.getDataPublicacao()).isEqualTo(LocalDate.of(1987, 11, 16));
        assertThat(livro.getAutor()).isEqualTo("Aluísio Azevedo");
    }

    @Test
    void to() {
        LivroDTO livroDTO = livroDTOConverter.to(LivroFixtures.LivroOProcesso());

        assertThat(livroDTO.getTitulo()).isEqualTo("O Processo");
        assertThat(livroDTO.getCategoria()).isEqualTo(CategoriaLivroEnum.DISTOPIA);
        assertThat(livroDTO.getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(livroDTO.getAutor()).isEqualTo("Franz Kakfa");
    }
}