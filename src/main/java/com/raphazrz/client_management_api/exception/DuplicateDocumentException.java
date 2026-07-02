package com.raphazrz.client_management_api.exception;

public class DuplicateDocumentException extends RuntimeException {
  public DuplicateDocumentException(String message) { super(message); }

    public int getStatusCode() {
      return 409;
    }
}
