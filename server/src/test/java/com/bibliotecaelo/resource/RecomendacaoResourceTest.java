package com.bibliotecaelo.resource;

import com.bibliotecaelo.ResourceTest;
import com.bibliotecaelo.converter.LivroDTOConverter;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.service.RecomendacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecomendacaoResourceTest extends ResourceTest {

    @MockBean
    RecomendacaoService recomendacaoService;

    @BeforeEach
    void setUpTest() {
        livroDTO = LivroFixtures.LivroDTOOCortico();
    }

    LivroDTO livroDTO;

    @Test
    void recomendacoesPorUsuario() throws Exception{
        Usuario usuario = UsuarioFixtures.usuarioPele();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        Page<Livro> pageToReturn = new PageImpl<>(List.of(new LivroDTOConverter().from(livroDTO)));
        Pageable pageable = Pageable.ofSize(20);

        when(recomendacaoService.getRecomendacoes(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"), pageable)).thenReturn(pageToReturn);

        mockMvc.perform(get("/api/recomendacoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", hasItem("22643a41-68b7-4eff-9893-75356d066a0b")))
                .andExpect(jsonPath("$.content[*].titulo", hasItem("O cortiço")))
                .andExpect(jsonPath("$.content[*].autor", hasItem("Aluísio Azevedo")))
                .andExpect(jsonPath("$.content[*].dataPublicacao", hasItem("1987-11-16")))
                .andExpect(jsonPath("$.content[*].categoria.descricao", hasItem("Romance")));

        verify(recomendacaoService).getRecomendacoes(UUID.fromString("f5070c94-c1ec-4be1-96cf-db855e3c5a1b"), pageable);
        verifyNoMoreInteractions(recomendacaoService);
    }
}