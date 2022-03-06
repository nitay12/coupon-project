package com.nitay.couponproject.exceptions;
/**
 * Thrown if the login credentials are wrong
 */
public class WrongCredentialsException extends Exception {
    public WrongCredentialsException() {
    }

    public WrongCredentialsException(String message) {
        super(message);
    }
}
