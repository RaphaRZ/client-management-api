package com.raphazrz.client_management_api.exception;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return 404;
    }
}
