package com.bibliotecaelo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bibliotecaelo.auth.validations.UserValidations;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.dto.usuario.UsuarioResponseDTO;
import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserValidations userValidations;

    UsuarioDTO usuarioDTO = UsuarioFixtures.usuarioCarmelitoDTO();
    Usuario usuario = UsuarioFixtures.usuarioPele();

    @Test
    void beforeSave() {
        assertThrows(IllegalStateException.class, () -> usuarioService.beforeSave(usuario));
    }

    @Test
    void novoUsuario() {
        when(passwordEncoder.encode(usuarioDTO.getSenha())).thenReturn("123");
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO usuarioResponseDTO = usuarioService.novoUsuario(usuarioDTO);

        assertThat(usuarioResponseDTO.getId()).isNotNull();
        assertThat(usuarioResponseDTO.getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(usuarioResponseDTO.getEmail()).isEqualTo("carmelito.benali@ig.com");
        assertThat(usuarioResponseDTO.getDataCadastro()).isEqualTo(LocalDate.of(1962, 9, 14));
        assertThat(usuarioResponseDTO.getTelefone()).isEqualTo("4499999999");
        assertThat(usuarioResponseDTO.getLogin()).isEqualTo("pele");

        verify(userValidations).validaUsuario(usuarioDTO);
        verify(userValidations).validaSenha(usuarioDTO.getSenha(), usuarioDTO.getSenhaConfirmacao());
        verify(passwordEncoder).encode(usuarioDTO.getSenha());
        verify(usuarioRepository).saveAndFlush(any(Usuario.class));
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void findById() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        Usuario usuarioFindById = usuarioService.findById(usuario.getId());

        assertThat(usuarioFindById.getId()).isEqualTo(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"));
        assertThat(usuarioFindById.getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(usuarioFindById.getEmail()).isEqualTo("carmelito.benali@ig.com");
        assertThat(usuarioFindById.getDataCadastro()).isEqualTo(LocalDate.of(1962, 9, 14));
        assertThat(usuarioFindById.getTelefone()).isEqualTo("4499999999");
        assertThat(usuarioFindById.getLogin()).isEqualTo("pele");

        verify(usuarioRepository).findById(usuario.getId());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void findByRsql() {
        Page<Usuario> pageToReturn = new PageImpl<>(List.of(usuario));
        String search = "nome=ilike=ozz";
        Pageable pageable = Pageable.ofSize(20);

        when(usuarioRepository.findByRsql(search, pageable)).thenReturn(pageToReturn);

        Page<Usuario> result = usuarioService.findByRsql(search, pageable);

        assertThat(result).extracting(Usuario::getId).containsOnlyOnce(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"));
        assertThat(result).extracting(Usuario::getNome).containsOnlyOnce("Edson Arantes do Nascimento");
        assertThat(result).extracting(Usuario::getEmail).containsOnlyOnce("carmelito.benali@ig.com");
        assertThat(result).extracting(Usuario::getDataCadastro).containsOnlyOnce(LocalDate.of(1962, 9, 14));
        assertThat(result).extracting(Usuario::getTelefone).containsOnlyOnce("4499999999");
        assertThat(result).extracting(Usuario::getLogin).containsOnlyOnce("pele");

        verify(usuarioRepository, times(1)).findByRsql(search, pageable);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void update() {
        Usuario usuarioToUpdate = usuario;
        usuarioToUpdate.setTelefone("987654321");
        usuarioToUpdate.setEmail("email.modificado@test.com.br");
        usuarioToUpdate.setSituacao(SituacaoUsuarioEnum.INATIVO);

        when(usuarioRepository.saveAndFlush(usuarioToUpdate)).thenReturn(usuarioToUpdate);

        Usuario usuarioUpdated = usuarioService.update(usuario);

        assertThat(usuarioUpdated.getId()).isEqualTo(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"));
        assertThat(usuarioUpdated.getNome()).isEqualTo("Edson Arantes do Nascimento");
        assertThat(usuarioUpdated.getEmail()).isEqualTo("email.modificado@test.com.br");
        assertThat(usuarioUpdated.getDataCadastro()).isEqualTo(LocalDate.of(1962, 9, 14));
        assertThat(usuarioUpdated.getTelefone()).isEqualTo("987654321");
        assertThat(usuarioUpdated.getLogin()).isEqualTo("pele");
        assertThat(usuarioUpdated.getSituacao()).isEqualTo(SituacaoUsuarioEnum.INATIVO);

        verify(usuarioRepository).saveAndFlush(usuarioToUpdate);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void deleteById() {
        usuarioService.deleteById(UUID.fromString("f3dbaf3c-46fd-459b-b4ba-71a585bf0e7a"));

        verify(usuarioRepository).deleteById(UUID.fromString("f3dbaf3c-46fd-459b-b4ba-71a585bf0e7a"));
        verifyNoMoreInteractions(usuarioRepository);
    }

}