package com.bibliotecaelo.service;

import java.util.UUID;

import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecomendacaoService {

    private final LivroRepository livroRepository;

    private final LivroDTOConverter livroDTOConverter;

    public Page<LivroDTO> getRecomendacoes(UUID usuarioId) {
        return livroRepository.getRecomendacoes(usuarioId, Pageable.ofSize(20))
                .map(livroDTOConverter::to);
    }
}
