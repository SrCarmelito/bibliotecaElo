package com.bibliotecaelo.service;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RecomendacaoServiceTest extends DefaultTest {

    @InjectMocks
    RecomendacaoService recomendacaoService;

    @Mock
    LivroRepository livroRepository;

    @Mock
    LivroDTOConverter livroDTOConverter;

    @Test
    void getRecomendacoes() {
        UUID usuarioId = UUID.randomUUID();

        when(livroRepository.livrosEmprestadosPorUsuarioId(usuarioId)).thenReturn(List.of());
        when(livroDTOConverter.to(any())).thenReturn(new LivroDTO());

        recomendacaoService.getRecomendacoes(usuarioId);

        verify(livroRepository).livrosEmprestadosPorUsuarioId(any());
        verifyNoMoreInteractions(livroRepository);
    }
}