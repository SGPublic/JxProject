package com.sgpublic.jxproject.exception;

public class JxConfigException extends JxIOException {
    public JxConfigException() {}

    public JxConfigException(String message){
        super(message);
    }

    public JxConfigException(String message, String args){
        super(String.format(message, args));
    }
}
