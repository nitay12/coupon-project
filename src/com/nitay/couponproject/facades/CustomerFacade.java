package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;

import java.util.ArrayList;

/**
 * A facade for all customer operations
 */
public class CustomerFacade extends ClientFacade {
    private long customerId;

    /**
     * @param email    Customer's email
     * @param password Customer's password
     * @return Boolean, True if log in succeed
     * @throws WrongCredentialsException If email/password not match
     * @throws CrudException             If there is any SQL exception
     */
    //TODO Remove side effect
    @Override
    public boolean login(String email, String password) throws WrongCredentialsException, CrudException {
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

    /**
     * Purchase a coupon
     * Adds the coupon to the purchase table
     * Reduce the coupon amount
     *
     * @param coupon The coupon to purchase, A full coupon object represent a coupon from the database
     * @return long - the purchased coupon id
     * @throws CrudException If there is any SQL exception
     */
    public long purchaseCoupon(Coupon coupon) throws CrudException {
        coupon.purchase();
        couponsDAO.updateCoupon(coupon);
        return couponsDAO.addCouponPurchase(customerId, coupon.getId());
    }

    /**
     * Gets all coupons of the logged in customer
     *
     * @return ArrayList user's coupons
     * @throws CrudException If there is any SQL exception
     */
    public ArrayList<Coupon> getCustomerCoupons() throws CrudException {
        return couponsDAO.getCustomerCoupons(customerId);
    }

    /**
     * Gets all the coupons of the logged in customer filtered by category
     *
     * @param category Category (enum) to filter the result
     * @return ArrayList of the customer's coupons filtered by given category
     * @throws CrudException If there is any SQL exception
     * @see Category
     */
    public ArrayList<Coupon> getCustomerCoupons(Category category) throws CrudException {
        return couponsDAO.getCustomerCoupons(customerId, category);
    }

    /**
     * Gets all the coupons of the logged in customer up to max price
     *
     * @param maxPrice The price to filter the result
     * @return ArrayList of the customer's coupons up to hte given maximum price
     * @throws CrudException If there is any SQL exception
     * @see Category
     */
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) throws CrudException {
        return couponsDAO.getCustomerCoupons(customerId, maxPrice);
    }

    /**
     * Gets the logged in customer details
     *
     * @return A Customer object with the logged in customer details
     * @throws CrudException If there is any SQL exception
     */
    public Customer getCustomerDetails() throws CrudException {
        return customersDAO.getOneCustomer(customerId);
    }
}
