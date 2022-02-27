package com.nitay.couponproject.enums;

public enum EntityType {
    COUPON("coupon"),
    CUSTOMER("customer"),
    COMPANY("company"),
    COUPON_PURCHASE("coupon purchase");
    public final String label;

    EntityType(String label) {
        this.label = label;
    }
}
