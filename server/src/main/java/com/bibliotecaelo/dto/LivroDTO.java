package com.bibliotecaelo.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.bibliotecaelo.interfaces.EntidadeDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LivroDTO implements EntidadeDTO {

    private UUID id;

    @Size(max = 1000)
    @NotBlank(message = "É Necessário Informar o Título do Livro!")
    private String titulo;

    @Size(max = 1000)
    @NotBlank(message = "É Necessário Informar o Autor do Livro!")
    private String autor;

    @Size(max = 13)
    @NotBlank(message = "É Necessário Informar o código ISBN do Livro!")
    private String isbn;

    @NotNull(message = "É Necessário Informar a Data de Publicação do Livro!")
    private LocalDate dataPublicacao;

    @NotNull(message = "É Necessário Informar a Categoria do Livro!")
    private CategoriaDTO categoria;
}
