package com.sgpublic.jxproject.exception;

public class JxManifestException extends JxIOException {
    public JxManifestException() {}

    public JxManifestException(String message){
        super(message);
    }

    public JxManifestException(String message, String args){
        super(String.format(message, args));
    }
}
