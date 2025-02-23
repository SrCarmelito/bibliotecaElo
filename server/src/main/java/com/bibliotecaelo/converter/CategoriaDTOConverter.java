package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.dto.CategoriaDTO;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class CategoriaDTOConverter implements DTOConverter<Categoria, CategoriaDTO> {

    @Override
    public Categoria from(CategoriaDTO dto, Categoria entity) {
        if (isNull(entity)) {
            entity = new Categoria();
        }

        entity.setId(dto.getId());
        entity.setDescricao(dto.getDescricao());

        return entity;
    }

    @Override
    public CategoriaDTO to(Categoria entity) {
        if (isNull(entity)) {
            return new CategoriaDTO();
        }

        CategoriaDTO dto = new CategoriaDTO();

        dto.setId(entity.getId());
        dto.setDescricao(entity.getDescricao());

        return dto;
    }
}
