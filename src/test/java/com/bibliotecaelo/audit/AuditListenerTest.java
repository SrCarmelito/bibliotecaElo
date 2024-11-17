package com.bibliotecaelo.audit;

import java.util.UUID;

import com.bibliotecaelo.converter.UsuarioDTOConverter;
import com.bibliotecaelo.dto.usuario.LoginDTO;
import com.bibliotecaelo.dto.usuario.UsuarioDTO;
import com.bibliotecaelo.dto.usuario.UsuarioResponseDTO;
import com.bibliotecaelo.fixtures.UsuarioFixtures;
import com.bibliotecaelo.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test")
@Transactional
@Sql(scripts = {
        "/sql/usuario.sql"
})
public class AuditListenerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioDTOConverter usuarioDTOConverter;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void testSetCreatedOn() throws Exception{

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("junior");
        loginDTO.setSenha("123");

        MvcResult mvcResult = mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andReturn();

        String token = mvcResult.getResponse().getContentAsString();

        UsuarioDTO usuarioDTO = UsuarioFixtures.usuarioCarmelitoDTO();

        MvcResult result = mockMvc.perform(post("/api/usuarios/novo-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO))
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String resultAsString = result.getResponse().getContentAsString();

        UsuarioResponseDTO usuarioResponseDTO = objectMapper.readValue(resultAsString, UsuarioResponseDTO.class);

        assertEquals("Carmelito Junior Delcielo Benali", usuarioRepository.findById(usuarioResponseDTO.getId()).orElseThrow().getAudit().getUsuarioAlteracao());
        assertEquals("Carmelito Junior Delcielo Benali", usuarioRepository.findById(usuarioResponseDTO.getId()).orElseThrow().getAudit().getUsuarioCriacao());
    }

    @Test
    @WithMockUser(username = "junior")
    public void testSetUpdatedOn() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("junior");
        loginDTO.setSenha("123");

        MvcResult mvcResult = mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andReturn();

        String token = mvcResult.getResponse().getContentAsString();

        UsuarioDTO usuarioAlterado = usuarioDTOConverter.to(
                usuarioRepository.findById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115")).orElseThrow());

        usuarioAlterado.setNome("Nome modificado");
        usuarioAlterado.setTelefone("1234567890");

        mockMvc.perform(put("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioAlterado))
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        assertEquals("junior", usuarioRepository.findById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115")).orElseThrow().getAudit().getUsuarioAlteracao());
        assertEquals("Nome modificado", usuarioRepository.findById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115")).orElseThrow().getNome());
        assertEquals("1234567890", usuarioRepository.findById(UUID.fromString("ee4ae880-a4db-4563-b330-7e2a27d26115")).orElseThrow().getTelefone());
    }
}