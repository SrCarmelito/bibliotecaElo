package com.bibliotecaelo.resource;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.ResourceTest;
import com.bibliotecaelo.converter.CategoriaDTOConverter;
import com.bibliotecaelo.domain.Categoria;
import com.bibliotecaelo.dto.CategoriaDTO;
import com.bibliotecaelo.fixtures.CategoriaFixtures;
import com.bibliotecaelo.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

class CategoriaResourceTest extends ResourceTest {

    @MockBean
    CategoriaService service;

    @Autowired
    CategoriaDTOConverter dtoConverter;

    @BeforeEach
    void setUpTest() {
        categoria = CategoriaFixtures.CategoriaPolicial();
        categoriaDTO = CategoriaFixtures.CategoriaDTORomance();
    }

    Categoria categoria;
    CategoriaDTO categoriaDTO;

    @Test
    void create() throws Exception {
        when(service.insert(any(Categoria.class))).thenReturn(dtoConverter.from(categoriaDTO));

        mockMvc.perform(post("/api/categorias")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.descricao", equalTo("Romance")));

        verify(service).insert((any(Categoria.class)));
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById() throws Exception {
        when(service.findById(UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"))).thenReturn(categoria);

        mockMvc.perform(get("/api/categorias/{categoriaId}", "be1ffc1e-aa98-4dce-9fa6-20233409b82d")
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("be1ffc1e-aa98-4dce-9fa6-20233409b82d")))
                .andExpect(jsonPath("$.descricao", equalTo("Policial")));

        verify(service).findById(UUID.fromString("be1ffc1e-aa98-4dce-9fa6-20233409b82d"));
        verifyNoMoreInteractions(service);
    }

    @Test
    void findAllSearch() throws Exception {
        Page<Categoria> pageToReturn = new PageImpl<>(List.of(categoria));
        Pageable pageable = Pageable.ofSize(20);
        String search = "descricao=ilike=cial";

        when(service.findByRsql(search, pageable)).thenReturn(pageToReturn);

        mockMvc.perform(get("/api/categorias/find?search=" + search)
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", hasItem("be1ffc1e-aa98-4dce-9fa6-20233409b82d")))
                .andExpect(jsonPath("$.content[*].descricao", hasItem("Policial")));

        verify(service).findByRsql(search, pageable);
        verifyNoMoreInteractions(service);
    }

    @Test
    void update() throws Exception {
        Categoria categoriaToUpdate = dtoConverter.from(categoriaDTO);
        when(service.findById(categoriaDTO.getId())).thenReturn(categoriaToUpdate);
        when(service.update(any(Categoria.class))).thenReturn(categoriaToUpdate);

        mockMvc.perform(put("/api/categorias")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("fc5944ed-84ea-40c1-9644-2b17ecb45eec")))
                .andExpect(jsonPath("$.descricao", equalTo("Romance")));

        verify(service).findById(categoriaDTO.getId());
        verify(service).update(categoriaToUpdate);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/api/categorias/{categoriaId}", "8a1c1ed0-46f4-4d8a-972a-7e8c9c825f23")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(service).deleteById(UUID.fromString("8a1c1ed0-46f4-4d8a-972a-7e8c9c825f23"));
        verifyNoMoreInteractions(service);
    }
}
