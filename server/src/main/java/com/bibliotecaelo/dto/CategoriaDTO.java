package com.bibliotecaelo.dto;

import java.util.UUID;

import com.bibliotecaelo.interfaces.EntidadeDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaDTO implements EntidadeDTO {

    private UUID id;

    @NotBlank(message = "É necessário informar a descricao da Categoria")
    private String descricao;

}
