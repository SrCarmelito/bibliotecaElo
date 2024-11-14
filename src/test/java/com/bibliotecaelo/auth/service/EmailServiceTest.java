package com.bibliotecaelo.auth.service;


import com.bibliotecaelo.DefaultTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailServiceTest extends DefaultTest {

    @Autowired
    EmailService emailService;

    @Test
    void enviarEmail() {
        String retorno = emailService.enviarEmail(
                "carmelito.benali@hotmail.com", "Assunto do e-mail", "Mensagem do e-mail");
        assertEquals("Email enviado", retorno);

        String retornoIncorreto = emailService.enviarEmail(
                "carmelito.benalihotmail.com", "Assunto do e-mail", "Mensagem do e-mail");

        assertThat(retornoIncorreto).contains("Erro ao enviar e-mail");
    }
}