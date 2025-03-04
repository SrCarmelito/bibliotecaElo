package com.bibliotecaelo.fixtures;

import java.util.UUID;

import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.dto.CategoriaDTO;

public class CategoriaFixtures {

    public static Categoria CategoriaPolicial() {
        Categoria categoria = new Categoria();

        categoria.setId(UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"));
        categoria.setDescricao("Policial");

        return categoria;
    }

    public static CategoriaDTO CategoriaDTORomance() {
        CategoriaDTO categoria = new CategoriaDTO();

        categoria.setId(UUID.fromString("fc5944ed-84ea-40c1-9644-2b17ecb45eec"));
        categoria.setDescricao("Romance");

        return categoria;
    }
}
