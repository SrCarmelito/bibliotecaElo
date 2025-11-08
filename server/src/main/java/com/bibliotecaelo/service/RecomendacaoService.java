package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecomendacaoService {

    private final LivroRepository livroRepository;

    public Page<Livro> getRecomendacoes(UUID usuarioId, Pageable pageable) {
        return livroRepository.getRecomendacoes(usuarioId, pageable);
    }
}
