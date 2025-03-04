package com.bibliotecaelo.converter;

import java.util.Objects;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.dto.EmprestimoDTO;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class EmprestimoDTOConverter
        implements DTOConverter<Emprestimo, EmprestimoDTO> {

    @Override
    public Emprestimo from(EmprestimoDTO dto, Emprestimo entity) {
        if (isNull(entity)) {
            entity = new Emprestimo();
        }

        if (Objects.nonNull(dto.getId())) {
            entity.setId(dto.getId());
            entity.setDataDevolucao(dto.getDataDevolucao());
            entity.setStatus(dto.getStatus());

            return entity;
        }

        entity.setUsuario(new UsuarioResponseDTOConverter().from(dto.getUsuario()));
        entity.setLivro(new LivroDTOConverter().from(dto.getLivro()));
        entity.setDataEmprestimo(dto.getDataEmprestimo());
        entity.setDataDevolucao(dto.getDataDevolucao());
        entity.setStatus(dto.getStatus());

        return entity;
    }

    @Override
    public EmprestimoDTO to(Emprestimo entity) {
        if (isNull(entity)) {
            return new EmprestimoDTO();
        }

        EmprestimoDTO dto = new EmprestimoDTO();

        dto.setId(entity.getId());
        dto.setUsuario(new UsuarioResponseDTOConverter().to(entity.getUsuario()));
        dto.setLivro(new LivroDTOConverter().to(entity.getLivro()));
        dto.setDataEmprestimo(entity.getDataEmprestimo());
        dto.setDataDevolucao(entity.getDataDevolucao());
        dto.setStatus(entity.getStatus());

        return dto;
    }
}
