package com.bibliotecaelo.enums;

public enum StatusEmprestimoEnum {

    CONCLUIDO("Concluído"),
    AGUARDANDO_DEVOLUCAO("Aguardando Devolução");

    private String value;

    StatusEmprestimoEnum(String value) {
        this.value = value;
    }
}
