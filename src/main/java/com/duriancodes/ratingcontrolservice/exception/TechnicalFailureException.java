package com.duriancodes.ratingcontrolservice.exception;

public class TechnicalFailureException extends RuntimeException {
    public TechnicalFailureException(String message) {
        super(message);
    }
}
