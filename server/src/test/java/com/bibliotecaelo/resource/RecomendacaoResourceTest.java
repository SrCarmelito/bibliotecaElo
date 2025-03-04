package com.bibliotecaelo.resource;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.ResourceTest;
import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.service.RecomendacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecomendacaoResourceTest extends ResourceTest {

    @MockBean
    RecomendacaoService recomendacaoService;

    LivroDTO livroDTO = LivroFixtures.LivroDTOOCortico();

    @Test
    void recomendacoesPorUsuario() throws Exception{
        Page<Livro> pageToReturn = new PageImpl<>(List.of(new LivroDTOConverter().from(livroDTO)));

        when(recomendacaoService.getRecomendacoes(UUID.fromString("a21069a7-0450-44bb-b88e-8d7d6ccf7ed7"))).thenReturn(pageToReturn);

        mockMvc.perform(get("/api/recomendacoes/{usuarioId}", "a21069a7-0450-44bb-b88e-8d7d6ccf7ed7")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", hasItem("22643a41-68b7-4eff-9893-75356d066a0b")))
                .andExpect(jsonPath("$.content[*].titulo", hasItem("O cortiço")))
                .andExpect(jsonPath("$.content[*].autor", hasItem("Aluísio Azevedo")))
                .andExpect(jsonPath("$.content[*].dataPublicacao", hasItem("1987-11-16")))
                .andExpect(jsonPath("$.content[*].categoria.descricao", hasItem("Romance")));

        verify(recomendacaoService).getRecomendacoes(UUID.fromString("a21069a7-0450-44bb-b88e-8d7d6ccf7ed7"));
        verifyNoMoreInteractions(recomendacaoService);
    }
}