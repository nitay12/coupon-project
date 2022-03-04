package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;

import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {
    private long customerId;

    @Override
    public boolean login(String email, String password) throws WrongCredentialsException {
        ArrayList<Customer> allCustomers = customersDAO.getAllCustomers();
        for (Customer customer :
                allCustomers) {
            if (email.equalsIgnoreCase(customer.getEmail()) && String.valueOf(password.hashCode()).equals(customer.getPassword())) {
                customerId = customer.getId();
                return true;
            }
        }
        throw new WrongCredentialsException("Wrong email or password");
    }

    public long purchaseCoupon(Coupon coupon) throws CrudException {
        coupon.purchase();
        couponsDAO.updateCoupon(coupon);
        return couponsDAO.addCouponPurchase(customerId, coupon.getId());
    }

    public ArrayList<Coupon> getCustomerCoupons() throws CrudException {
        return couponsDAO.getCustomerCoupons(customerId);
    }


    public ArrayList<Coupon> getCustomerCoupons(Category category) throws CrudException {
        return couponsDAO.getCustomerCoupons(customerId, category);
    }


    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws CrudException {
        return couponsDAO.getCustomerCoupons(customerId, maxPrice);
    }

    public Customer getCustomerDetails() {
        return customersDAO.getOneCustomer(customerId);
    }
}
