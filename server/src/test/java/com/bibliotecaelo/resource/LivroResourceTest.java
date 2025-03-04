package com.bibliotecaelo.resource;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.ResourceTest;
import com.bibliotecaelo.domain.Livro;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.service.LivroService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LivroResourceTest extends ResourceTest {

    @MockBean
    LivroService service;

    Livro livro = LivroFixtures.LivroOProcesso();
    LivroDTO livroDTO = LivroFixtures.LivroDTOOCortico();

    @Test
    void create() throws Exception {
        when(service.save(any(Livro.class))).thenReturn(livro);

        mockMvc.perform(post("/api/livros")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(livroDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.titulo", equalTo("O Processo")))
                .andExpect(jsonPath("$.autor", equalTo("Franz Kakfa")))
                .andExpect(jsonPath("$.dataPublicacao", equalTo("2010-05-17")))
                .andExpect(jsonPath("$.categoria.descricao", equalTo("Policial")));

        verify(service).save(any(Livro.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById() throws Exception {
        when(service.findById(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"))).thenReturn(livro);

        mockMvc.perform(get("/api/livros/{livroId}", "feb95cc3-8d9a-4cfb-be4e-8147fb195ec0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0")))
                .andExpect(jsonPath("$.titulo", equalTo("O Processo")))
                .andExpect(jsonPath("$.autor", equalTo("Franz Kakfa")))
                .andExpect(jsonPath("$.dataPublicacao", equalTo("2010-05-17")))
                .andExpect(jsonPath("$.categoria.descricao", equalTo("Policial")));

        verify(service).findById(UUID.fromString("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0"));
        verifyNoMoreInteractions(service);
    }

    @Test
    void findAllSearch() throws Exception {
        Page<Livro> pageToReturn = new PageImpl<>(List.of(livro));
        Pageable pageable = Pageable.ofSize(20);
        String search = "titulo=ilike=processo";

        when(service.findByRsql(search, pageable)).thenReturn(pageToReturn);

        mockMvc.perform(get("/api/livros/find?search=" + search)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", hasItem("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0")))
                .andExpect(jsonPath("$.content[*].titulo", hasItem("O Processo")))
                .andExpect(jsonPath("$.content[*].autor", hasItem("Franz Kakfa")))
                .andExpect(jsonPath("$.content[*].dataPublicacao", hasItem("2010-05-17")))
                .andExpect(jsonPath("$.content[*].categoria.descricao", hasItem("Policial")));

        verify(service).findByRsql(search, pageable);
        verifyNoMoreInteractions(service);
    }

    @Test
    void update() throws Exception {
        when(service.findById(livroDTO.getId())).thenReturn(livro);
        when(service.update(livro)).thenReturn(livro);

        mockMvc.perform(put("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.titulo", equalTo("O cortiço")))
                .andExpect(jsonPath("$.autor", equalTo("Aluísio Azevedo")))
                .andExpect(jsonPath("$.dataPublicacao", equalTo("1987-11-16")))
                .andExpect(jsonPath("$.categoria.descricao", equalTo("Romance")));

        verify(service).update(livro);
        verify(service).findById(livroDTO.getId());
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/api/livros/{livroId}", "c99e64bd-687f-45a3-8410-3109ffe04237")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(service).deleteById(UUID.fromString("c99e64bd-687f-45a3-8410-3109ffe04237"));
        verifyNoMoreInteractions(service);
    }

}
