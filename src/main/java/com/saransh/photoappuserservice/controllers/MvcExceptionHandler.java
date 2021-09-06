package com.saransh.photoappuserservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CryptSingh1337 on 9/2/2021
 */
@RequiredArgsConstructor
@ControllerAdvice
public class MvcExceptionHandler {

    private final ObjectMapper mapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationHandler(MethodArgumentNotValidException e) throws JsonProcessingException {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getFieldErrors())
            errors.put(error.getField(), error.getDefaultMessage());
        return ResponseEntity.badRequest()
                .body(
                        mapper.writeValueAsString(errors)
                );
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> invalidRequestPropertyHandler() {
        return ResponseEntity.badRequest().body("Invalid Property values");
    }
}
