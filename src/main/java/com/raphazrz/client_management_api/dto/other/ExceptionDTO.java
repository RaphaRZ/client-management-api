package com.raphazrz.client_management_api.dto.other;


import java.util.Map;

public record ExceptionDTO(
        String message,
        Map<String, String> validationErrors,
        int statusCode) {

    public ExceptionDTO(String message, int statusCode) {
        this(message, null, statusCode);
    }

    public ExceptionDTO(Map<String, String> validationErrors, int statusCode) {
        this("Validation failed.", validationErrors, statusCode);
    }
}