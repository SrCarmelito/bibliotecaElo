package com.bibliotecaelo.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class StandardError {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    @JsonIgnore
    private HttpStatus httpStatus;
    private String message;
    private String path;
    private Set<String> errors = new HashSet<>();

    public int getStatus() {
        return httpStatus.value();
    }

}