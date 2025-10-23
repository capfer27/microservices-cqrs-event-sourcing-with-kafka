package com.capfer.techbank.cqrs.core.exception;

public class AggregateNotFoundException extends RuntimeException {

    public AggregateNotFoundException() {
        super();
    }

    public AggregateNotFoundException(String message) {
        super(message);
    }
}
