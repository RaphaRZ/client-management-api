package com.raphazrz.client_management_api.dto.other;


public record ExceptionDTO(
        String message,
        int statusCode) {
}