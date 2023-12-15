package com.app.benevole.exception;

public class JwtUnsupportedException extends RuntimeException {
    public JwtUnsupportedException(String s) {
        super(s);
    }
}
