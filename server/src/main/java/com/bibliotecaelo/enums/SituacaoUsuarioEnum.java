package com.bibliotecaelo.enums;

import lombok.Getter;

@Getter
public enum SituacaoUsuarioEnum {

    ATIVO("Ativo"),
    INATIVO("Inativo");

    private final String value;

    SituacaoUsuarioEnum(String value) {
        this.value = value;
    }

}
