package com.nitay.couponproject.dal.interfaces;

import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;

import java.util.ArrayList;

public interface CouponsDAO {
    long addCoupon(Coupon coupon);
    void updateCoupon(Coupon coupon);
    void deleteCoupon(int couponID);

    void deleteCompanyCoupons(int companyID);

    ArrayList<Coupon> getAllCoupons();
    Coupon getOneCoupon(int couponID);
    long addCouponPurchase(long customerId, int couponId);

    //Separated this method into two methods for better implementation
    //void deleteCouponPurchase(int customerId, int couponId);

    void deleteCouponPurchase(int couponId);

    void deleteCustomerPurchase(long customerId);

    int getCategoryId(Category category);
}
