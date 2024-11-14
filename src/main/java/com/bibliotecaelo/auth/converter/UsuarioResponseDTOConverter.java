package com.bibliotecaelo.auth.converter;

import com.bibliotecaelo.auth.domain.Usuario;
import com.bibliotecaelo.auth.dto.UsuarioResponseDTO;
import org.apache.commons.lang3.NotImplementedException;

import static java.util.Objects.isNull;

public class UsuarioResponseDTOConverter implements DTOConverter<Usuario, UsuarioResponseDTO>{

    @Override
    public Usuario from(UsuarioResponseDTO dto, Usuario entity) {
        throw new NotImplementedException("UsuarioResponseDTOConverter.from Not Implemented YET");
    }

    @Override
    public UsuarioResponseDTO to(Usuario entity) {

        if(isNull(entity)) {
            return new UsuarioResponseDTO();
        }

        UsuarioResponseDTO dto = new UsuarioResponseDTO();

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
