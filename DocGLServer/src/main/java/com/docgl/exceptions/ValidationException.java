package com.docgl.exceptions;

/**
 * Created by Martin on 26.4.2017.
 * Exception
 */
public class ValidationException extends RuntimeException{
    public ValidationException(final String message){
        super(message);
    }
}
