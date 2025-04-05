package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class UsuarioDTOConverter
        implements DTOConverter<Usuario, UsuarioDTO> {
    @Override
    public Usuario from(UsuarioDTO dto, Usuario entity) {
        if (isNull(entity)) {
            entity = new Usuario();
        }

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setTelefone(dto.getTelefone());
        entity.setLogin(dto.getLogin());
        entity.setSituacao(dto.getSituacao());

        return entity;
    }

    @Override
    public UsuarioDTO to(Usuario entity) {

        if (isNull(entity)) {
            return new UsuarioDTO();
        }

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setDataNascimento(entity.getDataNascimento());
        dto.setTelefone(entity.getTelefone());
        dto.setLogin(entity.getLogin());
        dto.setSituacao(entity.getSituacao());

        return dto;
    }
}
