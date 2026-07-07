package com.raphazrz.client_management_api.exception;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException() {
        super("Contact not found.");
    }

    public int getStatusCode() {
        return 404;
    }
}
