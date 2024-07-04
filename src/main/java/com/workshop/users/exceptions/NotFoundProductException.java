package com.workshop.users.exceptions;


public class NotFoundProductException extends RuntimeException{

    public NotFoundProductException(String message){
        super(message);
    }

}
