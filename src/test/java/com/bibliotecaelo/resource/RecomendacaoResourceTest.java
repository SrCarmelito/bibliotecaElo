package com.bibliotecaelo.resource;

import java.util.UUID;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.service.RecomendacaoService;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecomendacaoResourceTest extends DefaultTest {

    @MockBean
    RecomendacaoService recomendacaoService;

    @Test
    public void recomendacoesPorUsuario() throws Exception{
        mockMvc.perform(get("/api/recomendacoes/{usuarioId}", "a21069a7-0450-44bb-b88e-8d7d6ccf7ed7")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(recomendacaoService).getRecomendacoes(UUID.fromString("a21069a7-0450-44bb-b88e-8d7d6ccf7ed7"));
        verifyNoMoreInteractions(recomendacaoService);
    }
}