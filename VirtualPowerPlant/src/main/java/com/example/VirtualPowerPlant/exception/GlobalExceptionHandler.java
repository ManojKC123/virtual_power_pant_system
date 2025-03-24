package com.example.VirtualPowerPlant.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleValidationException(ConstraintViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            errorMap.put(violation.getPropertyPath().toString(), violation.getMessage());
        });
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        BindingResult result = ex.getBindingResult();
        result.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMap.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, Object>> badRequest(String message) {
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleUniqueConstraintViolation(DataIntegrityViolationException ex) {
        if (ex.getMostSpecificCause().getMessage().contains("battery.name")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Battery with the same name already exists"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Data integrity violation: " + ex.getMostSpecificCause().getMessage()));
    }


}
