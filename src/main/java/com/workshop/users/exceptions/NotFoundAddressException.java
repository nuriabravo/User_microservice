package com.workshop.users.exceptions;

public class NotFoundAddressException extends RuntimeException{

    public NotFoundAddressException(String message){
        super(message);
    }

}

