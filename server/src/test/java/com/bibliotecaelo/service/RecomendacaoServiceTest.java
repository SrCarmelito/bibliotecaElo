package com.bibliotecaelo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.domain.Livro;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecomendacaoServiceTest {

    @InjectMocks
    RecomendacaoService recomendacaoService;

    @Mock
    LivroRepository livroRepository;

    @Test
    void getRecomendacoes() {
        UUID usuarioId = UUID.randomUUID();
        Page<Livro> pageToReturn = new PageImpl<>(List.of(LivroFixtures.LivroOProcesso()));

        when(livroRepository.getRecomendacoes(usuarioId, Pageable.ofSize(20))).thenReturn(pageToReturn);

        Page<Livro> result = recomendacaoService.getRecomendacoes(usuarioId);

        assertThat(result).extracting(Livro::getId).containsOnlyOnce(
                UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        assertThat(result).extracting(Livro::getTitulo).containsOnlyOnce("O Processo");
        assertThat(result).extracting(Livro::getAutor).containsOnlyOnce("Franz Kakfa");
        assertThat(result).extracting(Livro::getIsbn).containsOnlyOnce("6982568746");
        assertThat(result).extracting(Livro::getDataPublicacao).containsOnlyOnce(LocalDate.of(2010, 5, 17));
        assertThat(result).extracting(l -> l.getCategoria().getId()).containsOnlyOnce(
                UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"));
        assertThat(result).extracting(l -> l.getCategoria().getDescricao()).containsOnlyOnce("Policial");

        verify(livroRepository).getRecomendacoes(usuarioId, Pageable.ofSize(20));
        verifyNoMoreInteractions(livroRepository);
    }
}