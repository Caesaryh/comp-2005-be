package com.caesaryh.comp2005be.exception.type;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}