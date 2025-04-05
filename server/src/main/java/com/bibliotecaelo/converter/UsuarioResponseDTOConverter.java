package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.UsuarioResponseDTO;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class UsuarioResponseDTOConverter
        implements DTOConverter<Usuario, UsuarioResponseDTO> {

    @Override
    public Usuario from(UsuarioResponseDTO dto, Usuario entity) {
        if (isNull(entity)) {
            entity = new Usuario();
        }

        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setTelefone(dto.getTelefone());
        entity.setLogin(dto.getLogin());

        return entity;
    }

    @Override
    public UsuarioResponseDTO to(Usuario entity) {

        if (isNull(entity)) {
            return new UsuarioResponseDTO();
        }

        UsuarioResponseDTO dto = new UsuarioResponseDTO();

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
