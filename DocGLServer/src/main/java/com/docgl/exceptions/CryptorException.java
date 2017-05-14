package com.docgl.exceptions;

/**
 * Created by wdfeww on 4/29/17.
 * Cryptor Exception
 */
public class CryptorException extends RuntimeException {
    public CryptorException(final String message, Exception e){
        super(message, e);

    }
}
