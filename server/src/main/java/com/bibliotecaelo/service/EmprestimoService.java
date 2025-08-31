package com.bibliotecaelo.service;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.LivroRepository;
import com.bibliotecaelo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmprestimoService extends CrudService<Emprestimo> {

    @Getter
    private final EmprestimoRepository repository;
    private final UsuarioService usuarioService;
    private final LivroService livroService;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    @Override
    public void beforeSave(Emprestimo emprestimo) {
        validaDataEmprestimoPosteriorDevolucao(emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());

        if (repository.existsByLivroIdAndStatus(
                emprestimo.getLivro().getId(),
                StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO)) {
            throw new ValidationException("Livro Informado possui Empréstimo em andamento!");
        }

        emprestimo.setUsuario(usuarioRepository.findById(emprestimo.getUsuario().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário Não Encontrado!")));

        emprestimo.setLivro(livroRepository.findById(emprestimo.getLivro().getId())
                .orElseThrow(() -> new EntityNotFoundException("Livro Não Encontrado!")));
    }

    @Override public void beforeUpdate(Emprestimo emprestimo) {
        validaDataEmprestimoPosteriorDevolucao(emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());
    }

    @Override
    public void beforeDelete(UUID id) {
        throw new IllegalStateException("Não é permitido deletar um empréstimo.");
    }

    protected void validaDataEmprestimoPosteriorDevolucao(LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        if (dataEmprestimo.isAfter(dataDevolucao)) {
            throw new ValidationException("Data da Devolução menor que a data do Empréstimo, verifique!");
        }
    }

}
