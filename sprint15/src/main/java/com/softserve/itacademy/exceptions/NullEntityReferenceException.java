package com.softserve.itacademy.exceptions;

public class NullEntityReferenceException extends RuntimeException {
    public NullEntityReferenceException() {
    }

    public NullEntityReferenceException(String message) {
        super(message);
    }
}
