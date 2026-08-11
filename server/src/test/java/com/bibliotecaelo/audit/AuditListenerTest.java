package com.bibliotecaelo.audit;

import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.converter.UsuarioDTOConverter;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.dto.UsuarioDTO;
import com.bibliotecaelo.dto.UsuarioResponseDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
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

import java.time.LocalDate;
import java.util.UUID;

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
class AuditListenerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioDTOConverter usuarioDTOConverter;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    void testSetCreatedOn() throws Exception{

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("junior");
        loginDTO.setSenha("PassW0!");

        String token = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        UsuarioDTO usuarioDTO = UsuarioFixtures.usuarioCarmelitoDTO();

        String resultAsString = mockMvc.perform(post("/api/usuarios/novo-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO))
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn().getResponse().getContentAsString();

        UsuarioResponseDTO usuarioResponseDTO = objectMapper.readValue(resultAsString, UsuarioResponseDTO.class);

        Usuario usuarioAudit = usuarioRepository.findById(usuarioResponseDTO.getId()).orElseThrow();

        assertThat(usuarioAudit.getAudit().getUsuarioAlteracao()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuarioAudit.getAudit().getUsuarioCriacao()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuarioAudit.getAudit().getDataCriacao().toLocalDate()).isEqualTo(LocalDate.now());
        assertThat(usuarioAudit.getAudit().getDataAlteracao().toLocalDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void testSetUpdatedOn() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("junior");
        loginDTO.setSenha("PassW0!");

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String token = mvcResult.getResponse().getContentAsString();

        UsuarioDTO usuarioAlterado = usuarioDTOConverter.to(
                usuarioRepository.findById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115")).orElseThrow());

        usuarioAlterado.setNome("Nome modificado");
        usuarioAlterado.setTelefone("1234567890");
        usuarioAlterado.setSenha("123456As");

        mockMvc.perform(put("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioAlterado))
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        Usuario usuarioAudit = usuarioRepository.findById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115")).orElseThrow();

        assertThat(usuarioAudit.getNome()).isEqualTo("Nome modificado");
        assertThat(usuarioAudit.getTelefone()).isEqualTo("1234567890");

        assertThat(usuarioAudit.getAudit().getUsuarioAlteracao()).isEqualTo("Carmelito Junior Delcielo Benali");
        assertThat(usuarioAudit.getAudit().getUsuarioCriacao()).isEqualTo("system");
        assertThat(usuarioAudit.getAudit().getDataAlteracao().toLocalDate()).isEqualTo(LocalDate.now());
    }
}