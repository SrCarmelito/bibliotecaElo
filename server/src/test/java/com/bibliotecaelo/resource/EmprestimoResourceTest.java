package com.bibliotecaelo.resource;

import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.ResourceTest;
import com.bibliotecaelo.domain.Emprestimo;
import com.bibliotecaelo.dto.EmprestimoDTO;
import com.bibliotecaelo.fixtures.EmprestimoFixtures;
import com.bibliotecaelo.service.EmprestimoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmprestimoResourceTest
        extends ResourceTest {

    @MockBean
    EmprestimoService service;

    @BeforeEach
    void setUpTest() {
        emprestimo = EmprestimoFixtures.EmprestimoValido();
        emprestimoDTO = EmprestimoFixtures.EmprestimoDTOTeste();
    }

    Emprestimo emprestimo;
    EmprestimoDTO emprestimoDTO;

    @Test
    void create() throws Exception {
        when(service.insert((any(Emprestimo.class)))).thenReturn(emprestimo);

        mockMvc.perform(post("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emprestimoDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataEmprestimo", equalTo("2021-12-08")))
                .andExpect(jsonPath("$.dataDevolucao", equalTo("2024-08-07")))
                .andExpect(jsonPath("$.status", equalTo("CONCLUIDO")))
                .andExpect(jsonPath("$.usuario.id", equalTo("f5070c94-c1ec-4be1-96cf-db855e3c5a1b")))
                .andExpect(jsonPath("$.usuario.nome", equalTo("Edson Arantes do Nascimento")))
                .andExpect(jsonPath("$.usuario.email", equalTo("carmelito.benali@ig.com")))
                .andExpect(jsonPath("$.livro.id", equalTo("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0")))
                .andExpect(jsonPath("$.livro.titulo", equalTo("O Processo")))
                .andExpect(jsonPath("$.livro.categoria.id", equalTo("be1ffc1e-aa98-4dce-9fa6-20233409b82d")))
                .andExpect(jsonPath("$.livro.categoria.descricao", equalTo("Policial")));

        verify(service).insert((any(Emprestimo.class)));
        verifyNoMoreInteractions(service);
    }

    @Test
    void update() throws Exception {
        when(service.findById(emprestimoDTO.getId())).thenReturn(emprestimo);
        when(service.update(emprestimo)).thenReturn(emprestimo);

        mockMvc.perform(put("/api/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emprestimoDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("2cd3f08f-60c7-4214-ac82-b89550ed8992")))
                .andExpect(jsonPath("$.dataEmprestimo", equalTo("2024-11-15")))
                .andExpect(jsonPath("$.dataDevolucao", equalTo("2025-12-07")))
                .andExpect(jsonPath("$.status", equalTo("AGUARDANDO_DEVOLUCAO")))
                .andExpect(jsonPath("$.usuario.id", equalTo("054bf7ed-f9ba-4333-98fa-7d700e77526e")))
                .andExpect(jsonPath("$.usuario.nome", equalTo("Alex Martin")))
                .andExpect(jsonPath("$.usuario.email", equalTo("alex.martin@gmail.com")))
                .andExpect(jsonPath("$.livro.id", equalTo("22643a41-68b7-4eff-9893-75356d066a0b")))
                .andExpect(jsonPath("$.livro.titulo", equalTo("O cortiço")))
                .andExpect(jsonPath("$.livro.categoria.id", equalTo("fc5944ed-84ea-40c1-9644-2b17ecb45eec")))
                .andExpect(jsonPath("$.livro.categoria.descricao", equalTo("Romance")));

        verify(service).update(any(Emprestimo.class));
        verify(service).findById(emprestimoDTO.getId());
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById() throws Exception {
        when(service.findById(UUID.fromString("101d9817-c052-4fb4-ab76-d04a36179e6b"))).thenReturn(emprestimo);

        mockMvc.perform(get("/api/emprestimos/{emprestimoId}", "101d9817-c052-4fb4-ab76-d04a36179e6b")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataEmprestimo", equalTo("2021-12-08")))
                .andExpect(jsonPath("$.dataDevolucao", equalTo("2024-08-07")))
                .andExpect(jsonPath("$.status", equalTo("CONCLUIDO")))
                .andExpect(jsonPath("$.usuario.id", equalTo("f5070c94-c1ec-4be1-96cf-db855e3c5a1b")))
                .andExpect(jsonPath("$.usuario.nome", equalTo("Edson Arantes do Nascimento")))
                .andExpect(jsonPath("$.usuario.email", equalTo("carmelito.benali@ig.com")))
                .andExpect(jsonPath("$.livro.id", equalTo("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0")))
                .andExpect(jsonPath("$.livro.titulo", equalTo("O Processo")))
                .andExpect(jsonPath("$.livro.categoria.id", equalTo("be1ffc1e-aa98-4dce-9fa6-20233409b82d")))
                .andExpect(jsonPath("$.livro.categoria.descricao", equalTo("Policial")));

        verify(service).findById(UUID.fromString("101d9817-c052-4fb4-ab76-d04a36179e6b"));
        verifyNoMoreInteractions(service);
    }

    @Test
    void findAllSearch() throws Exception {
        Page<Emprestimo> pageToReturn = new PageImpl<>(List.of(emprestimo));
        Pageable pageable = Pageable.ofSize(20);
        String search = "usuario.nome=ilike=edson";

        when(service.findByRsql(any(), any())).thenReturn(pageToReturn);

        mockMvc.perform(get("/api/emprestimos/find?search=" + search)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].dataEmprestimo", hasItem("2021-12-08")))
                .andExpect(jsonPath("$.content[*].dataDevolucao", hasItem("2024-08-07")))
                .andExpect(jsonPath("$.content[*].status", hasItem("CONCLUIDO")))
                .andExpect(jsonPath("$.content[*].usuario.id", hasItem("f5070c94-c1ec-4be1-96cf-db855e3c5a1b")))
                .andExpect(jsonPath("$.content[*].usuario.nome", hasItem("Edson Arantes do Nascimento")))
                .andExpect(jsonPath("$.content[*].usuario.email", hasItem("carmelito.benali@ig.com")))
                .andExpect(jsonPath("$.content[*].livro.id", hasItem("feb95cc3-8d9a-4cfb-be4e-8147fb195ec0")))
                .andExpect(jsonPath("$.content[*].livro.titulo", hasItem("O Processo")))
                .andExpect(jsonPath("$.content[*].livro.categoria.id", hasItem("be1ffc1e-aa98-4dce-9fa6-20233409b82d")))
                .andExpect(jsonPath("$.content[*].livro.categoria.descricao", hasItem("Policial")));

        verify(service).findByRsql(search, pageable);
        verifyNoMoreInteractions(service);
    }
}