package com.workshop.users.exceptions;

public class AuthenticateException extends RuntimeException{
    public AuthenticateException(String message){
        super(message);
    }
}
