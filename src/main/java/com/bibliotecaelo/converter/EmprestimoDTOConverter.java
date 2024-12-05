package com.bibliotecaelo.converter;

import java.util.Objects;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.dto.EmprestimoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class EmprestimoDTOConverter
        implements DTOConverter<Emprestimo, EmprestimoDTO> {

    private final UsuarioResponseDTOConverter usuarioResponseDTOConverter;
    private final LivroDTOConverter livroDTOConverter;

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

        entity.setUsuario(usuarioResponseDTOConverter.from(dto.getUsuario()));
        entity.setLivro(livroDTOConverter.from(dto.getLivro()));
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
        dto.setUsuario(usuarioResponseDTOConverter.to(entity.getUsuario()));
        dto.setLivro(livroDTOConverter.to(entity.getLivro()));
        dto.setDataEmprestimo(entity.getDataEmprestimo());
        dto.setDataDevolucao(entity.getDataDevolucao());
        dto.setStatus(entity.getStatus());

        return dto;
    }
}
