package com.workshop.users.exceptions;

public class CantCreateCartException extends RuntimeException{
    public CantCreateCartException(String message) {
        super(message);
    }
}
