package com.sanil.electronic.store.exception;


public class ResourceNotFoundException extends  RuntimeException
{
    public ResourceNotFoundException()
    {

    }
    public ResourceNotFoundException(String message)
    {
        super(message);
    }

}
