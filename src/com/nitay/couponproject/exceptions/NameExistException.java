package com.nitay.couponproject.exceptions;

/**
 * Thrown if a Name already exist in the database
 */
public class NameExistException extends Exception {
    public NameExistException(String message) {
        super(message);
    }
}
