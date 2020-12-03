package com.sgpublic.jxproject.exception;

public class JxBroadcastException extends JxRuntimeException {
    public JxBroadcastException() {}

    public JxBroadcastException(String message){
        super(message);
    }

    public JxBroadcastException(String message, String args){
        super(String.format(message, args));
    }
}
