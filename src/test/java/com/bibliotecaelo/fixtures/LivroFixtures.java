package com.bibliotecaelo.fixtures;

import java.time.LocalDate;

import com.bibliotecaelo.domain.Livro;
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
}
