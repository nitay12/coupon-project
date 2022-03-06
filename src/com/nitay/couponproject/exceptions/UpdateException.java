package com.nitay.couponproject.exceptions;
/**
 * Thrown if there is an update restriction
 */
public class UpdateException extends Exception{
    public UpdateException() {
    }

    public UpdateException(String message) {
        super(message);
    }
}
