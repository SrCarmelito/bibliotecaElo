package com.bibliotecaelo.exceptions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerCustom {

    private void logConsoleMessage(Exception exception) {
        log.warn("{} message: ", exception.getClass().getSimpleName(), exception);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<StandardError> unknown(Exception exception, HttpServletRequest request) {

        String message = "Ops, ocorreu um erro inesperado.";
        logConsoleMessage(exception);
        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status).body(new StandardError(
                    status.value(),
                    message,
                    request.getRequestURI(),
                    Collections.singleton(message)
        ));
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            IllegalArgumentException.class,
            InvalidDataAccessApiUsageException.class,
            MethodArgumentTypeMismatchException.class,
            ValidationException.class,
    })
    private ResponseEntity<StandardError> general(Exception exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        logConsoleMessage(exception);

        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                exception.getLocalizedMessage(),
                request.getRequestURI(),
                Collections.singleton(exception.getLocalizedMessage())
        ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<StandardError> entityNotFoundException(EntityNotFoundException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        logConsoleMessage(exception);

        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                exception.getLocalizedMessage(),
                request.getRequestURI(),
                Collections.singleton(exception.getLocalizedMessage())
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityViolationException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        logConsoleMessage(exception);

        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                "Erro de integridade de dados.",
                request.getRequestURI(),
                Collections.singleton(Objects.requireNonNull(exception.getRootCause()).getMessage())
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        logConsoleMessage(exception);

        Set<String> errors = new HashSet<>();
        exception.getFieldErrors()
                .stream()
                .map(f -> errors.add(String.format("Campo %s %s ", f.getField(), f.getDefaultMessage())))
                .toList();

        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                "Requisição contém dados inválidos, verifique os erros e tente novamente.",
                request.getRequestURI(),
                errors)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<StandardError> constraintViolation(ConstraintViolationException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        logConsoleMessage(exception);

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        Set<String> errors = new HashSet<>(constraintViolations.size());
        errors.addAll(constraintViolations.stream()
                .map(violation -> String.format("%s %s %s", violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage()))
                .toList());
        
        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                "Requisição contém dados inválidos, verifique os erros e tente novamente.",
                request.getRequestURI(),
                errors)
        );
    }

    @ExceptionHandler(HttpClientErrorException.class)
    private ResponseEntity<StandardError> httpClientError(HttpClientErrorException exception, HttpServletRequest request) {
           
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logConsoleMessage(exception);

        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                "Não encontramos o que você precisava, tente novamente.", 
                request.getRequestURI(),
                Collections.singleton(exception.getLocalizedMessage()))
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    private ResponseEntity<StandardError> noResourceFound(NoResourceFoundException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        logConsoleMessage(exception);

        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                "Método inválido, verifique e tente novamente.",
                request.getRequestURI(),
                Collections.singleton(exception.getResourcePath() + " Não corresponde a nenhum end point."))
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<StandardError> httpMessageNotReadable(HttpMessageNotReadableException exception, HttpServletRequest request) {

       HttpStatus status = HttpStatus.BAD_REQUEST;
       logConsoleMessage(exception);

       return ResponseEntity.status(status).body(new StandardError(
               status.value(),
               "Erro estrutural da requisição, verifique o Json de payload e tente novamente.",
               request.getRequestURI(),
               Collections.singleton(exception.getMessage())
       ));
    }

    @ExceptionHandler(UnknownPropertyException.class)
    private ResponseEntity<StandardError> rsqlUnknownProperty(UnknownPropertyException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        logConsoleMessage(exception);

        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                exception.getMessage(),
                request.getRequestURI(),
                Collections.singleton(exception.getName()))
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<StandardError> badCredentials(BadCredentialsException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        logConsoleMessage(exception);

        return ResponseEntity.status(status).body(new StandardError(
                status.value(),
                exception.getMessage(),
                request.getRequestURI(),
                Collections.singleton("Senha incorreta."))
        );
    }
}
