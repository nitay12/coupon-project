package com.nitay.couponproject.exceptions;

/**
 * Thrown if an email already exist in the database
 */
public class EmailExistException extends Exception {
    public EmailExistException(String message) {
        super(message);
    }
}
