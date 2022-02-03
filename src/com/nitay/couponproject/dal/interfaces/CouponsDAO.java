package com.nitay.couponproject.dal.interfaces;

import com.nitay.couponproject.model.Coupon;

import java.util.ArrayList;

public interface CouponsDAO {
    long addCoupon(Coupon coupon);
    void updateCoupon(Coupon coupon);
    void deleteCoupon(int couponID);
    ArrayList<Coupon> getAllCoupons();
    Coupon getOneCoupon(int couponID);
    void addCouponPurchase(int customerId, int couponId);
    void deleteCouponPurchase(int customerId, int couponId);
}
