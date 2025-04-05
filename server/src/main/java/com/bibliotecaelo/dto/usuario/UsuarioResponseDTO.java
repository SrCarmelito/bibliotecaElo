package com.bibliotecaelo.dto.usuario;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.interfaces.EntidadeDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

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
