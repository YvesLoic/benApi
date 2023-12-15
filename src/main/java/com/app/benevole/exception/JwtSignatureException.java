package com.app.benevole.exception;

public class JwtSignatureException extends RuntimeException {
    public JwtSignatureException(String s) {
        super(s);
    }
}
