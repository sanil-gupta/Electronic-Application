package com.sanil.electronic.store.exception;

public class BadApiRequestException extends RuntimeException{
    public BadApiRequestException()
    {
     super("Bad Request !!");
    }
    public BadApiRequestException(String message)
    {
        super(message);
    }
}
