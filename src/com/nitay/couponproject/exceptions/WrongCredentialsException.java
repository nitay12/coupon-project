package com.nitay.couponproject.exceptions;

public class WrongCredentialsException extends Exception {
    public WrongCredentialsException() {
    }

    public WrongCredentialsException(String message) {
        super(message);
    }
}
