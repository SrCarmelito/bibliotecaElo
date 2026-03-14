package com.bibliotecaelo.consumer;

import com.bibliotecaelo.converter.PessoaDTOConverter;
import com.bibliotecaelo.domain.Pessoa;
import com.bibliotecaelo.dto.PessoaConsumerDTO;
import com.bibliotecaelo.repository.PessoaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PessoaConsumer {

    private final PessoaRepository repository;

    private final MessageMapperConverter converter;

    @RabbitListener(queues = "PESSOA_QUEUE")
    public void process(MessageConsumer message) throws JsonProcessingException {
        log.info("Iniciando consumo da fila PESSOA_QUEUE: {}", message);

        PessoaConsumerDTO pessoaConsumerDTO = converter.messaseToClass().readValue(message.getPayload(), PessoaConsumerDTO.class);

        if (message.getEntityEvent().equals(EntityEvent.DELETED)) {
            log.info("Deletando {}", pessoaConsumerDTO);
            repository.deleteByIdIntegration(pessoaConsumerDTO.getId());
            return;
        }

        Pessoa pessoaToSave = new PessoaDTOConverter().from(pessoaConsumerDTO, new Pessoa());
        repository.saveAndFlush(pessoaToSave);

        log.info("Finalizando consumo da fila PESSOA_QUEUE: {}", pessoaToSave);
    }

}
