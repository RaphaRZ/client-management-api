package com.raphazrz.client_management_api.exception;

public class DuplicateDocumentException extends RuntimeException {
    public DuplicateDocumentException() {
        super("Document already registered.");
    }

    public int getStatusCode() {
        return 409;
    }
}
