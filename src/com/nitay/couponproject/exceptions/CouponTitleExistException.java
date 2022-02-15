package com.nitay.couponproject.exceptions;

public class CouponTitleExistException extends Exception {
    public CouponTitleExistException() {
    }

    public CouponTitleExistException(String message) {
        super(message);
    }
}
