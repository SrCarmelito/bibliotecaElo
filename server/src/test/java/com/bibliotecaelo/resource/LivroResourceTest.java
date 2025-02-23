package com.bibliotecaelo.resource;

import com.bibliotecaelo.DefaultTest;
import com.bibliotecaelo.dto.LivroDTO;
import com.bibliotecaelo.fixtures.LivroFixtures;
import com.bibliotecaelo.service.LivroService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class LivroResourceTest
        extends DefaultTest {

    @MockBean
    private LivroService livroService;

    private final LivroDTO livroDTO = LivroFixtures.LivroDTOOCortico();

/*    @Test
    public void create() throws Exception {
        mockMvc.perform(post("/api/livros")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(livroDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(livroService).create(livroDTO);
        verifyNoMoreInteractions(livroService);
    }*/

    @Test
    public void findById() throws Exception {
        mockMvc.perform(get("/api/livros/{livroId}", "c99e64bd-687f-45a3-8410-3109ffe04237")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(livroService).findById(any());
        verifyNoMoreInteractions(livroService);
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(livroService).findAll(any());
        verifyNoMoreInteractions(livroService);
    }

/*    @Test
    public void update() throws Exception {
        mockMvc.perform(put("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroDTO)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(livroService).update(any());
        verifyNoMoreInteractions(livroService);
    }*/

    @Test
    public void deleteById() throws Exception {
        mockMvc.perform(delete("/api/livros/{livroId}", "c99e64bd-687f-45a3-8410-3109ffe04237")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(livroService).deleteById(any());
        verifyNoMoreInteractions(livroService);
    }

}
