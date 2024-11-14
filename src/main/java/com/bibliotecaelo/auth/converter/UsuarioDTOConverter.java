package com.bibliotecaelo.auth.converter;

import com.bibliotecaelo.auth.domain.Usuario;
import com.bibliotecaelo.auth.dto.UsuarioDTO;

import static java.util.Objects.isNull;

public class UsuarioDTOConverter implements DTOConverter<Usuario, UsuarioDTO>{
    @Override
    public Usuario from(UsuarioDTO dto, Usuario entity) {
        if (isNull(entity)) {
            entity = new Usuario();
        }

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setDataCadastro(dto.getDataCadastro());
        entity.setTelefone(dto.getTelefone());
        entity.setLogin(dto.getLogin());
        entity.setSituacao(dto.getSituacao());

        return entity;
    }

    @Override
    public UsuarioDTO to(Usuario entity) {

        if(isNull(entity)) {
            return new UsuarioDTO();
        }

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setTelefone(entity.getTelefone());
        dto.setLogin(entity.getLogin());
        dto.setSituacao(entity.getSituacao());

        return dto;
    }
}
