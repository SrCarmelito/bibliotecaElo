package com.bibliotecaelo.exceptions;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionHandlerCustom {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> general(Exception e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add("Ops, ocorreu um erro inesperado!");
        String message = "Ops, ocorreu um erro inesperado!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), message, request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrity(DataIntegrityViolationException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add(Objects.requireNonNull(e.getRootCause()).getMessage());
        String message = "Erro de integridade de Dados!";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(Instant.now(), status.value(), message, request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> argumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = "Requisição contém dados inválidos, verifique os erros e tente novamente!";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Set<String> errors = new HashSet<>();
        e.getFieldErrors()
                .stream()
                .map(f -> errors.add(String.format("Campo %s %s ", f.getField(), f.getDefaultMessage())))
                .toList();

        StandardError err = new StandardError(Instant.now(), status.value(), message, request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add(e.getLocalizedMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> constraintValidation(ConstraintViolationException e, HttpServletRequest request) {
        String message = "Requisição contém dados inválidos, verifique os erros e tente novamente!";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Set<String> errors = new HashSet<>(constraintViolations.size());
        errors.addAll(constraintViolations.stream()
                .map(violation -> String.format("%s %s %s", violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage()))
                .toList());

        StandardError err = new StandardError(Instant.now(), status.value() , message, request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> argumentNotValid(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add(e.getLocalizedMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<StandardError> httpClientError(HttpClientErrorException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        String message = "Não encontramos o que você precisava, tente novamente";
        errors.add(e.getLocalizedMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), message, request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<StandardError> noResourceFound(NoResourceFoundException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add(e.getResourcePath() + " Não corresponde a nenhum End Point!!!");
        String message = "Método inválido, verifique e tente novamente";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), message, request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add(e.getLocalizedMessage());
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> messageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        String message = "Erro Estrutural da requisição, verifique o Json de Payload e Tente novamente!";
        errors.add(e.getMessage());
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(Instant.now(), status.value(), message, request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardError> validationException(ValidationException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add(e.getLocalizedMessage());
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<StandardError> invalidDataAccessApiUsage(InvalidDataAccessApiUsageException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add(e.getLocalizedMessage());
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardError> requestNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        Set<String> errors = new HashSet<>();
        errors.add(e.getLocalizedMessage());
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI(), errors);
        return ResponseEntity.status(status).body(err);
    }
}
