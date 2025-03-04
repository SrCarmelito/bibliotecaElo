package com.bibliotecaelo.auth.dto;

import lombok.Data;

@Data
public class NewPasswordDTO {

    private String senha;
    private String senhaConfirmacao;
    private String token;

}
