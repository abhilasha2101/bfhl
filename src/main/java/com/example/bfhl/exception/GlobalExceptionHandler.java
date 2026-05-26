package com.example.bfhl.exception;

import com.example.bfhl.dto.BfhlResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BfhlResponse> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BfhlResponse> handleBadRequest(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BfhlResponse> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse());
    }

    private BfhlResponse buildErrorResponse() {
        return BfhlResponse.builder()
                .isSuccess(false)
                .userId(null)
                .email(null)
                .rollNumber(null)
                .oddNumbers(Collections.emptyList())
                .evenNumbers(Collections.emptyList())
                .alphabets(Collections.emptyList())
                .specialCharacters(Collections.emptyList())
                .sum("0")
                .concatString("")
                .build();
    }
}
