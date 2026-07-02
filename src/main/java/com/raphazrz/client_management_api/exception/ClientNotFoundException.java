package com.raphazrz.client_management_api.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return 404;
    }
}
