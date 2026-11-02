package com.bibliotecaelo.dto;

import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.interfaces.EntidadeDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UsuarioResponseDTO
        implements EntidadeDTO {

    private UUID id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String telefone;
    private String login;

    @Enumerated(EnumType.STRING)
    private SituacaoUsuarioEnum situacao;

}
