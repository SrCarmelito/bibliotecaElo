package com.bibliotecaelo.exceptions;

import io.github.perplexhub.rsql.UnknownPropertyException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerCustom {

    private static final String MESSAGE_REQUISICAO_DADOS_INVALIDOS = "Requisição contém dados inválidos, verifique os erros e tente novamente.";

    private ResponseEntity<StandardError> buildResponseError(
        Exception exception,
        HttpServletRequest request,
        HttpStatus httpStatus,
        String message,
        Set<String> errors
    ) {
        log.warn("{} message: ", exception.getClass().getSimpleName(), exception);

        StandardError standardError = new StandardError();
        standardError.setHttpStatus(httpStatus);
        standardError.setMessage(message);
        standardError.setPath(request.getRequestURI());
        standardError.setErrors(errors);

        return ResponseEntity.status(standardError.getStatus()).body(standardError);
    }

    @ExceptionHandler({
        Exception.class,
        EntityNotFoundException.class,
        HttpRequestMethodNotSupportedException.class,
        IllegalArgumentException.class,
        InvalidDataAccessApiUsageException.class,
        MethodArgumentTypeMismatchException.class,
        ValidationException.class,
    })
    private ResponseEntity<StandardError> general(Exception exception, HttpServletRequest request) {
        return buildResponseError(
            exception,
            request,
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            Collections.singleton(exception.getMessage())
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityViolationException exception, HttpServletRequest request) {
        return buildResponseError(
            exception, 
            request,
            HttpStatus.CONFLICT,
            "Erro de integridade de dados.",
            Collections.singleton(Objects.requireNonNull(exception.getRootCause()).getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {

        Set<String> errors = new HashSet<>();
        exception.getFieldErrors()
                .stream()
                .map(f -> errors.add(String.format("Campo %s %s ", f.getField(), f.getDefaultMessage())))
                .toList();
        
        return buildResponseError(
            exception, 
            request,
            HttpStatus.BAD_REQUEST,
            MESSAGE_REQUISICAO_DADOS_INVALIDOS,
            errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<StandardError> constraintViolation(ConstraintViolationException exception, HttpServletRequest request) {
        
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        Set<String> errors = new HashSet<>(constraintViolations.size());
        errors.addAll(constraintViolations.stream()
                .map(violation -> String.format("%s %s %s", violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage()))
                .toList());

        return buildResponseError(
            exception,
            request,
            HttpStatus.BAD_REQUEST,
            MESSAGE_REQUISICAO_DADOS_INVALIDOS,
            errors
        );
    }

    @ExceptionHandler(HttpClientErrorException.class)
    private ResponseEntity<StandardError> httpClientError(HttpClientErrorException exception, HttpServletRequest request) {
        return buildResponseError(
            exception,
            request,
            HttpStatus.BAD_REQUEST,
            "Não encontramos o que você precisava, tente novamente.",
            Collections.singleton(exception.getLocalizedMessage())
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    private ResponseEntity<StandardError> noResourceFound(NoResourceFoundException exception, HttpServletRequest request) {
        return buildResponseError(
            exception,
            request,
            HttpStatus.NOT_FOUND,
            "Método inválido, verifique e tente novamente.",
            Collections.singleton(exception.getResourcePath() + " Não corresponde a nenhum end point.")
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<StandardError> httpMessageNotReadable(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return buildResponseError(
            exception,
            request,
            HttpStatus.BAD_REQUEST,
            "Erro estrutural da requisição, verifique o Json de payload e tente novamente.",
            Collections.singleton(exception.getMessage())
        );
    }

    @ExceptionHandler(UnknownPropertyException.class)
    private ResponseEntity<StandardError> rsqlUnknownProperty(UnknownPropertyException exception, HttpServletRequest request) {
        return buildResponseError(
            exception,
            request,
            HttpStatus.BAD_REQUEST,
            exception.getMessage(),
            Collections.singleton(exception.getName())
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<StandardError> badCredentials(BadCredentialsException exception, HttpServletRequest request) {
        return buildResponseError(
            exception,
            request,
            HttpStatus.UNAUTHORIZED,
            exception.getMessage(),
            Collections.singleton("Senha incorreta.")
        );
    }
}
