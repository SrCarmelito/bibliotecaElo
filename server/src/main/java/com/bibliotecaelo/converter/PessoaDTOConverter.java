package com.bibliotecaelo.converter;

import com.bibliotecaelo.domain.Pessoa;
import com.bibliotecaelo.dto.PessoaConsumerDTO;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Objects.isNull;

@Component
public class PessoaDTOConverter {

    public Pessoa from(PessoaConsumerDTO dto, Pessoa entity) {
        if (isNull(entity)) {
            entity = new Pessoa();
        }

        if(isNull(entity.getId())) {
            entity.setId(UUID.randomUUID());
        }

        entity.setIdIntegration(dto.getId());
        entity.setNome(dto.getNome());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setCpf(dto.getCpf());
        entity.setTelefone(dto.getTelefone());

        return entity;
    }

    public PessoaConsumerDTO to(Pessoa entity) {
        throw new NotImplementedException("Método to não implementado");
    }

}
