package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
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

    public LivroService(LivroRepository repository, LivroDTOConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public LivroDTO create(LivroDTO livroDTO) {
        if (!repository.findByTitulo(livroDTO.getTitulo()).isEmpty()) {
            throw new ValidationException("Já Existe uma Livro Cadastrado com este Título!");
        }

        Livro novoLivro = converter.from(livroDTO);
        return converter.to(repository.save(novoLivro));
    }

    public LivroDTO findById(UUID livroId) {
        return converter.to(repository.findById(livroId).orElseThrow(
                () -> new EntityNotFoundException("Livro Não Encontrado a partir do id informado!")));
    }

    public Page<LivroDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(converter::to);
    }

    public LivroDTO update(LivroDTO livroDTO) {
        final Livro livroToUpdate = repository.findById(livroDTO.getId()).orElseThrow(
                () -> new EntityNotFoundException("Livro Não Encontrado a partir do id informado!"));

        return converter.to(repository.saveAndFlush(converter.from(livroDTO, livroToUpdate)));
    }

    public void deleteById(UUID livroId) {
        // TODO validar se o LIVRO tem registro de empréstimo, se tiver, não pode deixar deletar
        repository.deleteById(livroId);
    }
}
