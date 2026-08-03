package com.raphazrz.client_management_api.dto.other;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;


public record ExceptionDTO(
        @Schema(description = "Description of the error.")
        String message,

        @Schema(description = "Validation errors by field.")
        Map<String, String> validationErrors,

        @Schema(example = "404")
        int statusCode) {

    public ExceptionDTO(String message, int statusCode) {
        this(message, new HashMap<>(), statusCode);
    }

    public ExceptionDTO(Map<String, String> validationErrors, int statusCode) {
        this("Validation failed.", validationErrors, statusCode);
    }
}