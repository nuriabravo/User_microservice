package com.workshop.users.exceptions;

public class NotFoundUserException extends Exception{

    public NotFoundUserException(String message){
        super(message);
    }
}
