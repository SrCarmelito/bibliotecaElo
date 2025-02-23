package com.bibliotecaelo.enums;

import lombok.Getter;

@Getter
public enum SituacaoUsuarioEnum {

    ATIVO("ATIVO"),
    INATIVO("Inativo");

    private String value;

    SituacaoUsuarioEnum(String value) {
        this.value = value;
    }

}
