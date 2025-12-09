package com.bibliotecaelo.audit;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.audit.domain.Revision;
import com.bibliotecaelo.audit.repository.RevisionRepository;
import com.bibliotecaelo.converter.UsuarioDTOConverter;
import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {
        "/sql/usuario.sql"
})
class RevisionListenerImplTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    UsuarioRepository usuarioRepository;

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RevisionRepository revisionRepository;

    @Autowired
    UsuarioDTOConverter usuarioDTOConverter;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    void newRevision() throws Exception {
        executeRevision();

        Revision revision = revisionRepository.findById(1L).orElseThrow();

        assertThat(revision.getLogin()).isEqualTo("junior");
        assertThat(revision.getUserName()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(revision.getUserId()).isEqualTo(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115"));
        assertThat(revision.getRevisionDate().toLocalDate()).isEqualTo(LocalDate.now());
    }

    void executeRevision() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("junior");
        loginDTO.setSenha("PassW0!");

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andReturn();

        String token = mvcResult.getResponse().getContentAsString();

        UsuarioDTO usuarioAlterado = usuarioDTOConverter.to(
                usuarioRepository.findById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115")).orElseThrow());

        usuarioAlterado.setNome("Nome modificado");
        usuarioAlterado.setTelefone("1234567890");
        usuarioAlterado.setSenha("123456Aa");

        mockMvc.perform(put("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioAlterado))
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

}