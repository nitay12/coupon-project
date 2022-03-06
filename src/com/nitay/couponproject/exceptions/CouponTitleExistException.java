package com.nitay.couponproject.exceptions;

/**
 * Thrown if a coupon title already exist in the database
 */
public class CouponTitleExistException extends Exception {
    public CouponTitleExistException() {
    }

    public CouponTitleExistException(String message) {
        super(message);
    }
}
