package com.app.benevole.exception;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException(String s) {
        super(s);
    }
}
