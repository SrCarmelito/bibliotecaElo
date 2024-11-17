package com.bibliotecaelo.service;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.converter.EmprestimoDTOConverter;
import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.dto.Devolucao;
import com.bibliotecaelo.dto.EmprestimoDTO;
import com.bibliotecaelo.enums.StatusEmprestimoEnum;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.LivroRepository;
import com.bibliotecaelo.repository.UsuarioRepository;
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
        validaDataEmprestimoPosteriorDevolucao(emprestimoDTO.getDataEmprestimo(), emprestimoDTO.getDataEmprestimo());

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

    public EmprestimoDTO update(Devolucao devolucao) {

        Emprestimo emprestimoAtualizado = buscaEmprestimo(devolucao.getId());

        validaDataEmprestimoPosteriorDevolucao(
                emprestimoAtualizado.getDataEmprestimo(),
                devolucao.getDataDevolucao());

        emprestimoAtualizado.setDataDevolucao(devolucao.getDataDevolucao());
        emprestimoAtualizado.setStatus(devolucao.getStatus());

        return converter.to(emprestimoRepository.saveAndFlush(emprestimoAtualizado));
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

    protected void validaDataEmprestimoPosteriorDevolucao(LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        if(dataEmprestimo.isAfter(dataDevolucao)) {
            throw new ValidationException("Data da Devolução menor que a data do Empréstimo, verifique!");
        }
    }
}
