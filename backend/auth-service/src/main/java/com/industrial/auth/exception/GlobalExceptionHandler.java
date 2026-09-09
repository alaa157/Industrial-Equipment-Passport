package com.industrial.auth.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Map<String, Object> body(HttpStatus status, String error, String message) {
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("timestamp", Instant.now().toString());
        b.put("status", status.value());
        b.put("error", error);
        b.put("message", message);
        return b;
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<?> badCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(body(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_ERROR", "Invalid username/email or password."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> illegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(body(HttpStatus.BAD_REQUEST, "REQUEST_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> illegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(body(HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMITED", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        Map<String, Object> b = body(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed.");
        b.put("fields", errors);
        return ResponseEntity.badRequest().body(b);
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<?> notFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body(
                        HttpStatus.NOT_FOUND,
                        "NOT_FOUND",
                        ex.getMessage()));
    }
}
