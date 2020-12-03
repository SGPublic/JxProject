package com.sgpublic.jxproject.exception;

public class JxIOException extends JxRuntimeException {
    public JxIOException() {}

    public JxIOException(String message){
        super(message);
    }

    public JxIOException(String message, String args){
        super(String.format(message, args));
    }
}
