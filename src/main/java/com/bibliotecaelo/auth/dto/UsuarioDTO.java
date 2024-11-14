package com.bibliotecaelo.auth.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.auth.enums.SituacaoUsuarioEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    private UUID id;

    @NotBlank(message = "É Necessário Informar o Nome")
    @Size(min = 6, max = 150, message = "Nome deve ter entre 6 a 150 caracteres.")
    private String nome;

    @Email
    @NotBlank(message = "É Necessário Informar o E-mail")
    private String email;

    @NotNull(message = "Não é permitido Data de Cadastro Vazia!")
    @PastOrPresent(message = "Não é permitido Data de Cadastro no futuro!")
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @NotBlank(message = "É Necessário Informar o Telefone!")
    @Size(min = 10, max = 11, message = "Deve ser entre 10 e 11 caracteres com DDD")
    private String telefone;

    @NotBlank(message = "É Necessário Informar o login!")
    @Size(min = 6, max = 150, message = "Login deve ter entre 6 a 150 caracteres.")
    private String login;

    @NotBlank(message = "É Necessário Informar a senha!")
    @Size(min = 6, max = 150, message = "Senha deve ter entre 6 a 150 caracteres.")
    private String senha;
    private String senhaConfirmacao;

    @Enumerated(EnumType.STRING)
    private SituacaoUsuarioEnum situacao;

}
