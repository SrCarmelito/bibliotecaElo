package com.bibliotecaelo.converter;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.fixtures.LivroFixtures;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LivroDTOConverterTest {

    LivroDTOConverter livroDTOConverter = new LivroDTOConverter();

    @Test
    void from() {
        Livro livro = livroDTOConverter.from(LivroFixtures.LivroDTOOCortico());

        assertThat(livro.getId()).isEqualTo(UUID.fromString("22643a41-68b7-4eff-9893-75356d066a0b"));
        assertThat(livro.getTitulo()).isEqualTo("O cortiço");
        assertThat(livro.getCategoria().getDescricao()).isEqualTo("Romance");
        assertThat(livro.getDataPublicacao()).isEqualTo(LocalDate.of(1987, 11, 16));
        assertThat(livro.getAutor()).isEqualTo("Aluísio Azevedo");
    }

    @Test
    void to() {
        LivroDTO livroDTO = livroDTOConverter.to(LivroFixtures.LivroOProcesso());

        assertThat(livroDTO.getId()).isEqualTo(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        assertThat(livroDTO.getTitulo()).isEqualTo("O Processo");
        assertThat(livroDTO.getCategoria().getDescricao()).isEqualTo("Policial");
        assertThat(livroDTO.getDataPublicacao()).isEqualTo(LocalDate.of(2010, 5, 17));
        assertThat(livroDTO.getAutor()).isEqualTo("Franz Kakfa");
    }
}