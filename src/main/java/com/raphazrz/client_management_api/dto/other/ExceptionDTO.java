package com.raphazrz.client_management_api.dto.other;

import lombok.Getter;


@Getter
public class ExceptionDTO {
    private final String message;
    private final int statusCode;

    public ExceptionDTO(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
