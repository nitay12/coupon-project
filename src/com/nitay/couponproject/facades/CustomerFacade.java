package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;
import lombok.Setter;

import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {
    @Setter
    private long customerId;

    @Override
    public boolean login(String email, String password) throws WrongCredentialsException {
        ArrayList<Customer> allCustomers = customerDBDAO.getAllCustomers();
        for (Customer customer :
                allCustomers) {
            if (email.equalsIgnoreCase(customer.getEmail()) && String.valueOf(password.hashCode()).equals(customer.getPassword())) {
                customerId = customer.getId();
                return true;
            }
        }
        throw new WrongCredentialsException("Wrong email or password");
    }

    public long purchaseCoupon(Coupon coupon) {
        coupon.purchase();
        couponsDBDAO.updateCoupon(coupon);
        return couponsDBDAO.addCouponPurchase(customerId, coupon.getId());
    }

    private ArrayList<Coupon> getCustomerCoupons() {
        return couponsDBDAO.getCustomerCoupons(customerId);
    }

    ;

    private ArrayList<Coupon> getCustomerCoupons(Category category) {
        return couponsDBDAO.getCustomerCoupons(customerId, category);
    }

    ;

    private ArrayList<Coupon> getCustomerCoupons(double maxPrice) {
        return couponsDBDAO.getCustomerCoupons(customerId, maxPrice);
    }

    private Customer getCustomerDetails() {
        return customerDBDAO.getOneCustomer(customerId);
    }
}
