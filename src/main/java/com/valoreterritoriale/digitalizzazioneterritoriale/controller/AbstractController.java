package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractController {


    protected ResponseEntity<String> createResponse(String body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    protected ResponseEntity<String> createSuccessResponse(String body) {
        return createResponse(body, HttpStatus.OK);
    }

    protected ResponseEntity<String> createErrorResponse(String message, HttpStatus status) {
        return createResponse(message, status);
    }

    protected <T> ResponseEntity<List<T>> createListResponse(List<T> body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    protected <T> ResponseEntity<T> createObjectResponse(T object) {
        return ResponseEntity.ok(object);
    }
}