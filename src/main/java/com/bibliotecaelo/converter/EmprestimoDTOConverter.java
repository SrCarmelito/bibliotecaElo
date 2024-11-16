package com.bibliotecaelo.converter;

import com.bibliotecaelo.auth.converter.DTOConverter;
import com.bibliotecaelo.auth.converter.UsuarioResponseDTOConverter;
import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.dto.EmprestimoDTO;

import static java.util.Objects.isNull;

public class EmprestimoDTOConverter implements DTOConverter<Emprestimo, EmprestimoDTO> {
    @Override
    public Emprestimo from(EmprestimoDTO dto, Emprestimo entity) {
        if(isNull(entity)) {
            entity = new Emprestimo();
        }

        entity.setDataEmprestimo(dto.getDataEmprestimo());
        entity.setDataDevolucao(dto.getDataDevolucao());
        entity.setStatus(dto.getStatus());

        return entity;
    }

    @Override
    public EmprestimoDTO to(Emprestimo entity) {
        if(isNull(entity)) {
            return new EmprestimoDTO();
        }

        EmprestimoDTO dto = new EmprestimoDTO();

        dto.setId(entity.getId());

        UsuarioResponseDTOConverter usuarioResponseDTOConverter = new UsuarioResponseDTOConverter();
        dto.setUsuario(usuarioResponseDTOConverter.to(entity.getUsuario()));

        LivroDTOConverter livroDTOConverter = new LivroDTOConverter();
        dto.setLivro(livroDTOConverter.to(entity.getLivro()));
        dto.setDataEmprestimo(entity.getDataEmprestimo());
        dto.setDataDevolucao(entity.getDataDevolucao());
        dto.setStatus(entity.getStatus());

        return dto;
    }
}
