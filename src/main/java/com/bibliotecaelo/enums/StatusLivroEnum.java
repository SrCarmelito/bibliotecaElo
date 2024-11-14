package com.bibliotecaelo.enums;

public enum StatusLivroEnum {

    DISPONIVEL("Disponível"),
    EMPRESTADO("Emprestado");

    private String value;

    StatusLivroEnum(String value) {
        this.value = value;
    }
}
