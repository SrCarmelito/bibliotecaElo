package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.auth.repository.UsuarioRepository;
import com.bibliotecaelo.converter.EmprestimoDTOConverter;
import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.dto.EmprestimoDTO;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    private final EmprestimoDTOConverter converter;

    private final UsuarioRepository usuarioRepository;

    private final LivroRepository livroRepository;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            EmprestimoDTOConverter converter,
            UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.converter = converter;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    public EmprestimoDTO create(EmprestimoDTO emprestimoDTO) {
        validaDataEmprestimoPosteriorDevolucao(emprestimoDTO);

        if (!emprestimoRepository.findAllByLivroIdAndStatus(
                    emprestimoDTO.getLivro().getId(),
                    StatusEmprestimoEnum.AGUARDANDO_DEVOLUCAO)
                .isEmpty()) {
            throw new ValidationException("Livro Informado possui Empréstimo em andamento!");
        }

        Emprestimo novoEmprestimo = converter.from(emprestimoDTO);

        novoEmprestimo.setUsuario(usuarioRepository.findById(emprestimoDTO.getUsuario().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário Não Encontrado!")));

        novoEmprestimo.setLivro(livroRepository.findById(emprestimoDTO.getLivro().getId())
                .orElseThrow(() -> new EntityNotFoundException("Livro Não Encontrado!")));

        return converter.to(emprestimoRepository.save(novoEmprestimo));
    }

    public EmprestimoDTO update(EmprestimoDTO emprestimoDTO) {
        validaDataEmprestimoPosteriorDevolucao(emprestimoDTO);

        final Emprestimo emprestimoToUpdate = buscaEmprestimo(emprestimoDTO.getId());

        validaAlteracoes(emprestimoToUpdate, emprestimoDTO);

        return converter.to(emprestimoRepository
                .saveAndFlush(converter.from(emprestimoDTO, emprestimoToUpdate)));
    }

    public EmprestimoDTO findById(UUID emprestimoId) {
        return converter.to(buscaEmprestimo(emprestimoId));
    }

    public Page<EmprestimoDTO> findAll(Pageable pageable) {
        return emprestimoRepository.findAll(pageable).map(converter::to);
    }

    protected Emprestimo buscaEmprestimo(UUID emprestimoId) {
        return emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new ValidationException("Empréstimo Não Encontrado"));
    }

    protected void validaDataEmprestimoPosteriorDevolucao(EmprestimoDTO emprestimoDTO) {
        if(emprestimoDTO.getDataEmprestimo().isAfter(emprestimoDTO.getDataDevolucao())) {
            throw new ValidationException("Data da Devolução menor que a data do Empréstimo, verifique!");
        }
    }
    protected void validaAlteracoes(Emprestimo emprestimoToUpdate, EmprestimoDTO emprestimoDTO) {
        if(!emprestimoToUpdate.getUsuario().getId().equals(emprestimoDTO.getUsuario().getId())) {
            throw new ValidationException("Não é permitido alterar o usuário que emprestou o Livro!");
        }

        if (!emprestimoToUpdate.getLivro().getId().equals(emprestimoDTO.getLivro().getId())) {
            throw new ValidationException("Não é permitido alterar o Livro do Empréstimo!");
        }

        if(!emprestimoToUpdate.getDataEmprestimo().isEqual(emprestimoDTO.getDataEmprestimo())) {
            throw new ValidationException("Não é permitido alterar a data do Empréstimo!");
        }
    }

}
