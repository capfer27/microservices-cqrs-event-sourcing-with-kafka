package com.capfer.techbank.cqrs.core.exception;

public class ConcurrencyException extends RuntimeException {

    public ConcurrencyException() {
        super();
    }

    public ConcurrencyException(String message) {
        super(message);
    }
}
