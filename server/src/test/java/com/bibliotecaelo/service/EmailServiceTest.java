package com.bibliotecaelo.service;

import com.bibliotecaelo.auth.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    void enviarEmailSuccess() {
        String retorno = emailService.enviarEmail(
                "carmelito.benali@hotmail.com", "Assunto do e-mail", "Mensagem do e-mail");

        assertThat(retorno).contains("Email enviado");
    }

    @Test
    void enviarEmailError() {
        String retornoIncorreto = emailService.enviarEmail(
                "carmelito.benalihotmail.com", "Assunto do e-mail", "Mensagem do e-mail");

        assertThat(retornoIncorreto).contains("Erro ao enviar e-mail");
    }
}