package com.example.atharv.config;



import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Map<String, String>> handleJsonException(JsonProcessingException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "JSON processing error");
        response.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}