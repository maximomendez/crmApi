package com.pathmonk.crmapi.web.problem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import jakarta.persistence.EntityExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ProblemDetails problem = new ProblemDetails(
                "https://docs.crm.example/errors/validation",
                "Validation failed",
                422,
                detail,
                ex.getParameter().toString(),
                Instant.now());
        return ResponseEntity.status(422).body(problem);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetails> handleNotFound(NoSuchElementException ex) {
        ProblemDetails problem = new ProblemDetails(
                "https://docs.crm.example/errors/not-found",
                "Resource not found",
                404,
                ex.getMessage(),
                "",
                Instant.now());
        return ResponseEntity.status(404).body(problem);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ProblemDetails> handleAlreadyExists(EntityExistsException ex) {
        ProblemDetails problem = new ProblemDetails(
                "https://docs.crm.example/errors/already-exists",
                "Resource already exists",
                409,
                ex.getMessage(),
                "",
                Instant.now());
        return ResponseEntity.status(409).body(problem);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetails> handleConflict(IllegalStateException ex) {
        ProblemDetails problem = new ProblemDetails(
                "https://docs.crm.example/errors/conflict",
                "Conflict",
                409,
                ex.getMessage(),
                "",
                Instant.now());
        return ResponseEntity.status(409).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleOther(Exception ex) {
        ProblemDetails problem = new ProblemDetails(
                "https://docs.crm.example/errors/internal",
                "Internal error",
                500,
                ex.getMessage(),
                "",
                Instant.now());
        return ResponseEntity.status(500).body(problem);
    }
}