package com.bibliotecaelo.fixtures;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.enums.CategoriaLivroEnum;

public class LivroFixtures {

    public static Livro LivroOProcesso() {
        Livro livro = new Livro();

        livro.setId(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        livro.setTitulo("O Processo");
        livro.setAutor("Franz Kakfa");
        livro.setIsbn(6982568746L);
        livro.setDataPublicacao(LocalDate.of(2010, 5, 17));
        livro.setCategoria(CategoriaLivroEnum.DISTOPIA);

        return livro;
    }

    public static LivroDTO LivroDTOOCortico() {
        LivroDTO livroDTO = new LivroDTO();

        livroDTO.setId(UUID.fromString("22643a41-68b7-4eff-9893-75356d066a0b"));
        livroDTO.setTitulo("O cortiço");
        livroDTO.setAutor("Aluísio Azevedo");
        livroDTO.setIsbn(9095506304069L);
        livroDTO.setDataPublicacao(LocalDate.of(1987, 11, 16));
        livroDTO.setCategoria(CategoriaLivroEnum.FICCAO_CIENTIFICA);

        return livroDTO;
    }
}
