package com.sgpublic.jxproject.exception;

public class JxRecourseException extends JxIOException {
    public JxRecourseException() {}

    public JxRecourseException(String message){
        super(message);
    }

    public JxRecourseException(String message, String args){
        super(String.format(message, args));
    }

    public JxRecourseException(String message, Object ... args){
        super(String.format(message, args));
    }
}
