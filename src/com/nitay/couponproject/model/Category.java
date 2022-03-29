package com.nitay.couponproject.model;

import lombok.ToString;
//TODO deprecate this enum and make an object that initialized the categories from the DB when the app starts
/**
 * Enum that represent the categories table in the database
 * Used in the Creation of coupons and when filtering coupons results
 */
@ToString
public enum Category {
    Food,
    Electricity,
    Restaurant,
    Vacation
}
