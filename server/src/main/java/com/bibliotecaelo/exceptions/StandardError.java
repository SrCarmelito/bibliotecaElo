package com.bibliotecaelo.exceptions;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class StandardError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private final Instant timestamp = Instant.now();
    private int status;
    private String message;
    private String path;
    private Set<String> errors = new HashSet<>();

    public StandardError(int status, String message, String path, Set<String> errors) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }
}