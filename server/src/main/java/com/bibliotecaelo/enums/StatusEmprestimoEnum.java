package com.bibliotecaelo.enums;

import lombok.Getter;

@Getter
public enum StatusEmprestimoEnum {

    CONCLUIDO("Concluído"),
    AGUARDANDO_DEVOLUCAO("Aguardando devolução");

    private final String value;

    StatusEmprestimoEnum(String value) {
        this.value = value;
    }
}
