package com.bibliotecaelo.service;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecomendacaoServiceTest {

    @InjectMocks
    RecomendacaoService recomendacaoService;

    @Mock
    LivroRepository livroRepository;

    @Mock
    LivroDTOConverter livroDTOConverter;

    @Test
    void getRecomendacoes() {
        UUID usuarioId = UUID.randomUUID();
        Livro livro = LivroFixtures.LivroOProcesso();
        List<Livro> list = List.of(livro);

        Page<Livro> page = new PageImpl<Livro>(list, Pageable.ofSize(20), 10);

        when(livroRepository.getRecomendacoes(usuarioId, Pageable.ofSize(20))).thenReturn(page);
        when(livroDTOConverter.to(LivroFixtures.LivroOProcesso())).thenReturn(new LivroDTO());

        recomendacaoService.getRecomendacoes(usuarioId);

        verify(livroDTOConverter).to(livro);
        verify(livroRepository).getRecomendacoes(usuarioId, Pageable.ofSize(20));
        verifyNoMoreInteractions(livroRepository);
    }
}