package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.auth.validations.UserValidations;
import com.bibliotecaelo.converter.UsuarioDTOConverter;
import com.bibliotecaelo.converter.UsuarioResponseDTOConverter;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.dto.usuario.UsuarioResponseDTO;
import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.repository.UsuarioRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService extends CrudService<Usuario> {

    @Getter
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidations userValidations;

    @Override
    public void beforeSave(Usuario usuario) {
        throw new IllegalStateException("Utilize o end-point /novo-usuario para criar um novo usuário!");
    }

    @Override
    public void beforeDelete(UUID id) {
        throw new IllegalStateException("Não é permitido apagar o cadastro de um usuário!");
    }

    @Override
    public void beforeUpdate(Usuario entity) {
        entity.setSenha(passwordEncoder.encode(entity.getSenha()));
    }

    public UsuarioResponseDTO novoUsuario(UsuarioDTO usuarioDTO) {
        userValidations.validaUsuario(usuarioDTO);
        userValidations.validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao());

        Usuario novoUsuario = new UsuarioDTOConverter().from(usuarioDTO);

        novoUsuario.setSituacao(SituacaoUsuarioEnum.INATIVO);
        novoUsuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        return new UsuarioResponseDTOConverter().to(repository.saveAndFlush(novoUsuario));
    }

}
