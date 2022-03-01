package com.nitay.couponproject.tasks;

import com.nitay.couponproject.dal.interfaces.CouponsDAO;
import com.nitay.couponproject.model.Coupon;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class CouponExpirationDailyJob implements Runnable {
    private boolean quit;
    private CouponsDAO couponsDAO;
    private final int SLEEP_TIME = 5000;
//            86400000; //<- A day in millis
    public CouponExpirationDailyJob(CouponsDAO couponsDAO) {
        this.couponsDAO = couponsDAO;
    }

    @Override
    public void run() {
        while (!quit) {
            System.out.println("stated daily job");
            ArrayList<Coupon> coupons = couponsDAO.getAllCoupons();
            for (Coupon coupon :
                    coupons) {
                Date endDate = coupon.getEndDate();
                Date now = Date.valueOf(LocalDate.now());
                if (endDate.after(now)){
                    couponsDAO.deleteCouponPurchase(coupon.getId());
                    couponsDAO.deleteCoupon(coupon.getId());
                    System.out.println("Deleted coupon id:" + coupon.getId());
                }
            }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    public void stop() {
    }
}
