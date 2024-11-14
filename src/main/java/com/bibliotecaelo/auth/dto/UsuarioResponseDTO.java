package com.bibliotecaelo.auth.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.auth.enums.SituacaoUsuarioEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UsuarioResponseDTO {

    private UUID id;
    private String nome;
    private String email;
    private LocalDate dataCadastro;
    private String telefone;
    private String login;

    @Enumerated(EnumType.STRING)
    private SituacaoUsuarioEnum situacao;

}
