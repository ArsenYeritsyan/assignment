package org.technamin.assignment.exceptions;

public class MongoProcessException extends RuntimeException {
    public MongoProcessException(Throwable cause) {
        super(cause);
    }

    public MongoProcessException(String message) {
        super(message);
    }
}
