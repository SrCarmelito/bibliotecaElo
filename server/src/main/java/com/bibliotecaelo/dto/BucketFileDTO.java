package com.bibliotecaelo.dto;

import com.bibliotecaelo.interfaces.EntidadeDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Data
public class BucketFileDTO implements EntidadeDTO {

    private UUID id;

    @Size(max = 1000)
    private String nome;
    private UUID fileId;
    private long size;
    private String contentType;

    @JsonIgnore
    private InputStream inputStream;
    private MultipartFile multipartFile;

}
