package com.bibliotecaelo.fixtures;

import java.time.LocalDate;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.enums.CategoriaLivroEnum;

public class LivroFixtures {

    public static Livro LivroOProcesso() {
        Livro livro = new Livro();

        livro.setTitulo("O Processo");
        livro.setAutor("Franz Kakfa");
        livro.setIsbn(6982568746L);
        livro.setDataPublicacao(LocalDate.of(2010, 5, 17));
        livro.setCategoria(CategoriaLivroEnum.DISTOPIA);

        return livro;
    }

    public static LivroDTO LivroDTOOCortico() {
        LivroDTO livroDTO = new LivroDTO();

        livroDTO.setTitulo("O cortiço");
        livroDTO.setAutor("Aluísio Azevedo");
        livroDTO.setIsbn(9095506304069L);
        livroDTO.setDataPublicacao(LocalDate.of(1987, 11, 16));
        livroDTO.setCategoria(CategoriaLivroEnum.FICCAO_CIENTIFICA);

        return livroDTO;
    }
}
