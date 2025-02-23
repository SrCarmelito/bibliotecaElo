package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class LivroDTOConverter implements DTOConverter<Livro, LivroDTO> {

    private final CategoriaDTOConverter categoriaDTOConverter;
    @Override
    public Livro from(LivroDTO dto, Livro entity) {
        if (isNull(entity)) {
            entity = new Livro();
        }

        entity.setId(dto.getId());
        entity.setTitulo(dto.getTitulo());
        entity.setAutor(dto.getAutor());
        entity.setIsbn(dto.getIsbn());
        entity.setDataPublicacao(dto.getDataPublicacao());

        if (nonNull(dto.getCategoria())) {
            entity.setCategoria(categoriaDTOConverter.from(dto.getCategoria()));
        }

        return entity;
    }

    @Override
    public LivroDTO to(Livro entity) {

        if (isNull(entity)) {
            return new LivroDTO();
        }

        LivroDTO dto = new LivroDTO();

        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setAutor(entity.getAutor());
        dto.setIsbn(entity.getIsbn());
        dto.setDataPublicacao(entity.getDataPublicacao());
        dto.setCategoria(categoriaDTOConverter.to(entity.getCategoria()));

        return dto;
    }
}
