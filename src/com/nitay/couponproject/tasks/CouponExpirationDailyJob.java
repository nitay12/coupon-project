package com.nitay.couponproject.tasks;

import com.nitay.couponproject.dal.interfaces.CouponsDAO;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.model.Coupon;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A task that runs everyday and deletes the expired coupons
 */
public class CouponExpirationDailyJob implements Runnable {
    @Getter
    @Setter
    private boolean stop = false;
    @Getter
    @Setter
    private boolean quit = false;
    private final CouponsDAO couponsDAO;
    private final int DAY_IN_MILLIS = 86400000; //<- A day in millis
    private final int SLEEP_TIME = 10000;

    public CouponExpirationDailyJob(CouponsDAO couponsDAO) {
        this.couponsDAO = couponsDAO;
    }

    @Override
    public void run() {

        try {
            while (!quit) {
                while (!stop) {
                    ArrayList<Coupon> coupons = couponsDAO.getAllCoupons();
                    for (Coupon coupon :
                            coupons) {
                        Date endDate = coupon.getEndDate();
                        Date now = Date.valueOf(LocalDate.now());
                        if (endDate.before(now)) {
                            couponsDAO.deletePurchaseByCouponId(coupon.getId());
                            couponsDAO.deleteCoupon(coupon.getId());
                            System.out.println("COUPON EXPIRATION JOB - Deleted coupon id:" + coupon.getId() + " (current date:" + now.toString() + ", coupons expiration:" + coupon.getEndDate().toString());
                        }
                    }
                    Thread.sleep(SLEEP_TIME);
                }
            }
        } catch (InterruptedException | CrudException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        setStop(true);
    }
    public void quit() {
        setQuit(true);
    }
}
