package com.bibliotecaelo.dto.usuario;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.interfaces.EntidadeDTO;
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
public class UsuarioDTO implements EntidadeDTO {

    private UUID id;

    @NotBlank(message = "É necessário informar o nome.")
    @Size(min = 6, max = 150, message = "Nome deve ter entre 6 a 150 caracteres.")
    private String nome;

    @Email
    @NotBlank(message = "É necessário informar o e-mail.")
    private String email;

    @NotNull(message = "Não é permitido data de cadastro vazia.")
    @PastOrPresent(message = "Não é permitido data de cadastro no futuro.")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotBlank(message = "É necessário informar o telefone.")
    @Size(min = 10, max = 11, message = "Deve ser entre 10 e 11 caracteres com DDD.")
    private String telefone;

    @NotBlank(message = "É necessário informar o login.")
    @Size(min = 6, max = 15, message = "Login deve ter entre 6 a 15 caracteres.")
    private String login;

    @NotBlank(message = "É necessário informar a senha.")
    @Size(min = 6, max = 15, message = "Senha deve ter entre 6 a 15 caracteres.")
    private String senha;

    private String senhaConfirmacao;

    @Enumerated(EnumType.STRING)
    private SituacaoUsuarioEnum situacao;

}
