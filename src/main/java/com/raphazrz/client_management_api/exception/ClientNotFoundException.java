package com.raphazrz.client_management_api.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException() {
        super("Client not found.");
    }

    public int getStatusCode() {
        return 404;
    }
}
