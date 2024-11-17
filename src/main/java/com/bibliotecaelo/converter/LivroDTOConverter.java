package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;

import static java.util.Objects.isNull;

public class LivroDTOConverter implements DTOConverter<Livro, LivroDTO> {

    @Override
    public Livro from(LivroDTO dto, Livro entity) {
        if (isNull(entity)) {
            entity = new Livro();
        }

        entity.setTitulo(dto.getTitulo());
        entity.setAutor(dto.getAutor());
        entity.setIsbn(dto.getIsbn());
        entity.setDataPublicacao(dto.getDataPublicacao());
        entity.setCategoria(dto.getCategoria());

        return entity;
    }

    @Override
    public LivroDTO to(Livro entity) {

        if(isNull(entity)) {
            return new LivroDTO();
        }

        LivroDTO dto = new LivroDTO();

        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setAutor(entity.getAutor());
        dto.setIsbn(entity.getIsbn());
        dto.setDataPublicacao(entity.getDataPublicacao());
        dto.setCategoria(entity.getCategoria());

        return dto;
    }
}
