package com.nitay.couponproject.enums;

import com.nitay.couponproject.exceptions.CrudException;

/**
 * All entities in the database, used in CrudException
 *
 * @see CrudException
 */
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
