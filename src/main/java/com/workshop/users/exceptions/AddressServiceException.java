package com.workshop.users.exceptions;

public class AddressServiceException extends RuntimeException {
    public AddressServiceException(String message) {
        super(message);
    }
}