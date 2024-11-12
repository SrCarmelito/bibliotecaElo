package com.bibliotecaelo.auth.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String enviarEmail(String destinatario, String assunto, String mensagem) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagem, true);
            javaMailSender.send(message);
            return "Email enviado";
        } catch (Exception e) {
            log.info(" essa aqui é mensagem doido.... {}", e.getLocalizedMessage());
            e.printStackTrace();
            return "Erro ao enviar e-mail " + e.getLocalizedMessage();
        }
    }

}
