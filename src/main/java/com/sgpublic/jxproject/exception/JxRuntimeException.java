package com.sgpublic.jxproject.exception;

public class JxRuntimeException extends RuntimeException {
    public JxRuntimeException() {}

    public JxRuntimeException(String message){
        super(message);
    }

    public JxRuntimeException(String message, String args){
        super(String.format(message, args));
    }
}
