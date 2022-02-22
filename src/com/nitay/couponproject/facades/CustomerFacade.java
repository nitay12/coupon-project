package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;

import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {
    private int customerId;
    @Override
    protected boolean login(String email, String password) throws WrongCredentialsException {
        ArrayList<Customer> allCustomers = customerDBDAO.getAllCustomers();
        for (Customer customer :
                allCustomers) {
            if (email.equalsIgnoreCase(customer.getEmail()) && String.valueOf(password.hashCode()).equals(customer.getPassword())) {
                return true;
            }
        }
        throw new WrongCredentialsException("Wrong email or password");
    }
    private long purchaseCoupon(Coupon coupon){
        return couponsDBDAO.addCouponPurchase(customerId, coupon.getId());
    }
    private ArrayList<Coupon> getCustomerCoupons(){
        return couponsDBDAO.getCustomerCoupons(customerId);
    };
    private ArrayList<Coupon> getCustomerCoupons(Category category){
        return couponsDBDAO.getCustomerCoupons(customerId, category);
    };
    private ArrayList<Coupon> getCustomerCoupons(double maxPrice){
        return couponsDBDAO.getCustomerCoupons(customerId,maxPrice);
    }
    private Customer getCustomerDetails(){
        return customerDBDAO.getOneCustomer(customerId);
    }
}
