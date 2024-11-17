package com.bibliotecaelo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.converter.EmprestimoDTOConverter;
import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.EmprestimoAtualizadoDTO;
import com.bibliotecaelo.dto.EmprestimoDTO;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.fixtures.EmprestimoFixtures;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.LivroRepository;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class EmprestimoServiceTest extends DefaultTest {

    @InjectMocks
    EmprestimoService service;

    @Mock
    EmprestimoRepository emprestimoRepository;

    @Mock
    LivroRepository livroRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    EmprestimoDTOConverter converter;

    EmprestimoDTO emprestimoDTO = EmprestimoFixtures.EmprestimoDTOTeste();

    Emprestimo emprestimo = EmprestimoFixtures.EmprestimoValido();

    EmprestimoAtualizadoDTO emprestimoAtualizadoDTO = EmprestimoFixtures.emprestimoAtualizadoDTO();

    @Test
    void create() {
        emprestimo.setStatus(StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);

        when(emprestimoRepository.findAllByLivroIdAndStatus(any(), any())).thenReturn(List.of(new Emprestimo()));

        String mensagemLivroComEmprestimoAndamento = assertThrows(ValidationException.class,
                () -> service.create(emprestimoDTO)).getMessage();

        assertThat(mensagemLivroComEmprestimoAndamento).isEqualTo("Livro Informado possui Empréstimo em andamento!");
    }

    @Test
    void createThrows() {
        when(converter.from(emprestimoDTO)).thenReturn(emprestimo);
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(UsuarioFixtures.usuarioPele()));
        when(livroRepository.findById(any())).thenReturn(Optional.of(new Livro()));

        service.create(emprestimoDTO);

        verify(emprestimoRepository).findAllByLivroIdAndStatus(any(), any());
        verify(livroRepository).findById(emprestimoDTO.getLivro().getId());
        verify(emprestimoRepository).save(emprestimo);
        verifyNoMoreInteractions(emprestimoRepository);
    }

    @Test
    void update() {
        when(emprestimoRepository.findById(any())).thenReturn(Optional.of(emprestimo));

        service.update(emprestimoAtualizadoDTO);

        verify(emprestimoRepository).findById(emprestimoAtualizadoDTO.getId());
        verify(emprestimoRepository).saveAndFlush(any());
        verifyNoMoreInteractions(emprestimoRepository);
    }

    @Test
    void findById() {
        when(emprestimoRepository.findById(any())).thenReturn(Optional.of(new Emprestimo()));
        when(converter.to(any())).thenReturn(emprestimoDTO);

        EmprestimoDTO emprestimoFindById = service.findById(emprestimoDTO.getId());

        assertThat(emprestimoFindById.getDataEmprestimo()).isEqualTo(LocalDate.of(2024, 11, 15));
        assertThat(emprestimoFindById.getDataDevolucao()).isEqualTo(LocalDate.of(2025, 12, 7));
        assertThat(emprestimoFindById.getStatus()).isEqualTo(StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO);
        assertThat(emprestimoFindById.getUsuario().getNome()).isEqualTo("Alex Martin");
        assertThat(emprestimoFindById.getLivro().getDataPublicacao()).isEqualTo(LocalDate.of(1987, 11, 16));
    }

    @Test
    void findAll() {
        Page<Emprestimo> page = Page.empty();
        Pageable pageable = Pageable.unpaged();

        when(emprestimoRepository.findAll(pageable)).thenReturn(page);

        service.findAll(pageable);

        verify(emprestimoRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(emprestimoRepository);
    }

    @Test
    void validaDataEmprestimoPosteriorDataDevolucao() {
        emprestimoAtualizadoDTO.setDataDevolucao(LocalDate.of(6000, 12, 1));

        String mensagemAlteracaoDaData = assertThrows(ValidationException.class,
                () -> service.validaDataEmprestimoPosteriorDevolucao(
                        emprestimoAtualizadoDTO.getDataDevolucao(),
                        LocalDate.of(2024, 12, 1)
                )).getMessage();

        assertThat(mensagemAlteracaoDaData).isEqualTo("Data da Devolução menor que a data do Empréstimo, verifique!");
    }

}