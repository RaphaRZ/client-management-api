package com.raphazrz.client_management_api.exception;

import com.raphazrz.client_management_api.dto.other.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ExceptionDTO exceptionDTO = new ExceptionDTO(errors, e.getStatusCode().value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Request body could not be read.", 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Invalid value for parameter '" + e.getName() + "'.", 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

    @ExceptionHandler(InvalidContactTypeException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidContactTypeException(InvalidContactTypeException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), e.getStatusCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleClientNotFoundException(ClientNotFoundException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), e.getStatusCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleContactNotFoundException(ContactNotFoundException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), e.getStatusCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Requested HTTP method is not supported for this endpoint.", 405);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(exceptionDTO);
    }

    @ExceptionHandler(DuplicateDocumentException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateDocumentException(DuplicateDocumentException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), e.getStatusCode());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionDTO);
    }


}
