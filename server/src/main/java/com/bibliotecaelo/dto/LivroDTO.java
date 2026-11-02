package com.bibliotecaelo.dto;

import com.bibliotecaelo.interfaces.EntidadeDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LivroDTO implements EntidadeDTO {

    private UUID id;

    @Size(max = 1000)
    @NotBlank(message = "É necessário informar o título do livro.")
    private String titulo;

    @Size(max = 1000)
    @NotBlank(message = "É necessário informar o autor do livro.")
    private String autor;

    @Size(max = 13)
    @NotBlank(message = "É necessário informar o código ISBN do livro.")
    private String isbn;

    @NotNull(message = "É necessário informar a data de publicação do livro.")
    private LocalDate dataPublicacao;

    private BucketFileDTO bucketFile;

    @NotNull(message = "É necessário informar a categoria do livro.")
    private CategoriaDTO categoria;
}
