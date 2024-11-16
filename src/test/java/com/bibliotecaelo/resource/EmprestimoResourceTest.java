package com.bibliotecaelo.resource;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.dto.EmprestimoDTO;
import com.bibliotecaelo.fixtures.EmprestimoFixtures;
import com.bibliotecaelo.service.EmprestimoService;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmprestimoResourceTest extends DefaultTest {

    @MockBean
    private EmprestimoService service;

    private final  EmprestimoDTO emprestimoDTO = EmprestimoFixtures.EmprestimoDTOTeste();

    @Test
    public void create() throws Exception {
        mockMvc.perform(post("/api/emprestimos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emprestimoDTO)))
                        .andDo(print())
                        .andExpect(status().is2xxSuccessful());

        verify(service).create(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(put("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emprestimoDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(service).update(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(get("/api/emprestimos/{emprestimoId}", "101d9817-c052-4fb4-ab76-d04a36179e6b")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(service).findById(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(service).findAll(any());
        verifyNoMoreInteractions(service);
    }
}