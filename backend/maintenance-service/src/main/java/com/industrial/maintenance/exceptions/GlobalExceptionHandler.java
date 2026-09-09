package com.industrial.maintenance.exceptions;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> body(
            HttpStatus status,
            String error,
            String message) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return body;
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<?> notFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body(
                        HttpStatus.NOT_FOUND,
                        "NOT_FOUND",
                        ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> illegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(body(
                        HttpStatus.BAD_REQUEST,
                        "REQUEST_ERROR",
                        ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> illegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(body(
                        HttpStatus.CONFLICT,
                        "CONFLICT",
                        ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = body(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Request validation failed.");

        body.put("fields", errors);

        return ResponseEntity.badRequest().body(body);
    }
}