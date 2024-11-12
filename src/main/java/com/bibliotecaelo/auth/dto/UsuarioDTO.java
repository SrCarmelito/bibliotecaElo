package com.bibliotecaelo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDTO {

    @NotBlank(message = "É Necessário Informar o Nome")
    @Size(min = 6, max = 150, message = "Deve entre 6 a 150 caracteres.")
    private String nome;

    @Email
    @NotBlank(message = "É Necessário Informar o E-mail")
    private String email;

    @NotBlank
    @Size(min = 6, max = 150, message = "Login deve ter entre 6 a 150 caracteres.")
    private String login;

    @NotBlank
    @Size(min = 6, max = 150, message = "Senha deve ter entre 6 a 150 caracteres.")
    private String senha;

    @NotBlank
    @Size(min = 6, max = 150, message = "Confirmação de Senha deve ter entre 6 a 150 caracteres.")
    private String senhaConfirmacao;

}
