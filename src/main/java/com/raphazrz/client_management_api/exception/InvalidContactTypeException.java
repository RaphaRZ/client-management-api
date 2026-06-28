package com.raphazrz.client_management_api.exception;

public class InvalidContactTypeException extends RuntimeException {
    public InvalidContactTypeException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return 400;
    }
}
