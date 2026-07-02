package com.raphazrz.client_management_api.exception;

import com.raphazrz.client_management_api.dto.other.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(DuplicateDocumentException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateDocumentException(DuplicateDocumentException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), e.getStatusCode());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionDTO);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Request body could not be read.", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);

    }
}
