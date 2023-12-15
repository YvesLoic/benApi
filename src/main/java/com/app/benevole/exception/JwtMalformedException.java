package com.app.benevole.exception;

public class JwtMalformedException extends RuntimeException {
    public JwtMalformedException(String s) {
        super(s);
    }
}
