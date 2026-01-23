package com.example.demo.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,Object> handleNotFound(ResourceNotFoundException ex){
        return Map.of(
                "status", 404,
                "message", ex.getMessage(),
                "time", LocalDateTime.now()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handleRuntime(RuntimeException ex){
        return Map.of(
                "status", 400,
                "message", ex.getMessage(),
                "time", LocalDateTime.now()
        );
    }
}
