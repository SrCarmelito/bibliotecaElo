package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.repository.EmprestimoRepository;
import com.bibliotecaelo.repository.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    private final LivroRepository repository;
    private final LivroDTOConverter converter;
    private final EmprestimoRepository emprestimoRepository;


    public LivroService(LivroRepository repository, LivroDTOConverter converter,
            EmprestimoRepository emprestimoRepository) {
        this.repository = repository;
        this.converter = converter;
        this.emprestimoRepository = emprestimoRepository;
    }

    public LivroDTO create(LivroDTO livroDTO) {
        if (!repository.findByTitulo(livroDTO.getTitulo()).isEmpty()) {
            throw new ValidationException("Já Existe uma Livro Cadastrado com este Título!");
        }

        return converter.to(repository.save(converter.from(livroDTO)));
    }

    public LivroDTO findById(UUID livroId) {
        return converter.to(buscaLivro(livroId));
    }

    public Page<LivroDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(converter::to);
    }

    public LivroDTO update(LivroDTO livroDTO) {
        final Livro livroToUpdate = buscaLivro(livroDTO.getId());

        return converter.to(repository.saveAndFlush(converter.from(livroDTO, livroToUpdate)));
    }

    public void deleteById(UUID livroId) {
        if (emprestimoRepository.existsByLivroId(livroId)) {
            throw new ValidationException("Livro Possui Empréstimo vinculado, portanto NÃO será excluído!");
        }

        repository.deleteById(livroId);
    }

    private Livro buscaLivro(UUID livroId) {
        return repository.findById(livroId).orElseThrow(
                () -> new EntityNotFoundException("Livro Não Encontrado a partir do id informado!"));
    }
}
