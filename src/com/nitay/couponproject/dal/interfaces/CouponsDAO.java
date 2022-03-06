package com.nitay.couponproject.dal.interfaces;

import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;

import java.util.ArrayList;

/**
 * An interface for coupons data access object
 */
public interface CouponsDAO {
    /**
     * Adds a new coupon to the database
     *
     * @param coupon A Coupon object (no Id required)
     * @return Auto generated coupon ID
     * @throws CrudException if something gets wrong.
     * @see Coupon
     * @see CrudException
     */
    long addCoupon(Coupon coupon) throws CrudException;

    /**
     * Updates an existing coupon in the database
     *
     * @param coupon A full Coupon object (Id param is required)
     * @throws CrudException if something gets wrong.
     * @see Coupon
     * @see CrudException
     */
    void updateCoupon(Coupon coupon) throws CrudException;

    /**
     * Deletes a coupon from the database
     *
     * @param couponID The coupon id in the database
     * @throws CrudException if something gets wrong.
     * @see CrudException
     */
    void deleteCoupon(int couponID) throws CrudException;

    /**
     * Deletes all company's coupons
     *
     * @param companyID The company id
     * @throws CrudException if something gets wrong.
     * @see CrudException
     */
    void deleteCompanyCoupons(int companyID) throws CrudException;

    /**
     * Gets all coupons that exist in the database
     *
     * @return An ArrayList of all coupons
     * @throws CrudException if something gets wrong.
     * @see Coupon
     * @see CrudException
     */
    ArrayList<Coupon> getAllCoupons() throws CrudException;

    /**
     * Gets a coupon from the database by given ID
     *
     * @param couponID The coupon id in the database
     * @return A Coupon object
     * @throws CrudException if something gets wrong
     * @see Coupon
     * @see CrudException
     */
    Coupon getOneCoupon(int couponID) throws CrudException;

    /**
     * Add a one coupon purchase by one customer
     *
     * @param customerId The customer id (must exist in the database)
     * @param couponId   The coupon id (must exist in the database)
     * @return The Coupon id
     * @throws CrudException if something gets wrong
     * @see CrudException
     */
    long addCouponPurchase(long customerId, int couponId) throws CrudException;

    //I separated the delete purchase method into two methods for better implementation

    /**
     * Deletes a coupon purchase
     *
     * @param couponId The coupon id
     * @throws CrudException if something gets wrong.
     */
    void deletePurchaseByCouponId(long couponId) throws CrudException;

    /**
     * Deletes a coupon purchase
     *
     * @param customerId The customer id
     * @throws CrudException if something gets wrong
     */

    void deletePurchaseByCustomerId(long customerId) throws CrudException;

    /**
     * Gets all coupons of a given company
     *
     * @param companyId The company id in the database
     * @return ArrayList of all company's coupons
     */
    ArrayList<Coupon> getCompanyCoupons(long companyId) throws CrudException;

    /**
     * Gets all the given company's coupons filtered by given category
     *
     * @param companyId The company id in the database
     * @param category  A Category (enum) refers the category column in the database
     * @return ArrayList of all company's coupons filtered by category
     */

    ArrayList<Coupon> getCompanyCoupons(long companyId, Category category) throws CrudException;

    /**
     * Gets all coupons of the given company up to the given maximum price
     *
     * @param companyId The company id in the database
     * @param maxPrice  Maximum price to filter the result
     * @return ArrayList of all company's coupons up to maxPrice
     * @throws CrudException if something gets wrong
     */
    ArrayList<Coupon> getCompanyCoupons(long companyId, double maxPrice) throws CrudException;

    /**
     * Gets all given customer's coupons
     *
     * @param customerId The customer id in the database
     * @return An ArrayList of all given customer's coupons
     * @throws CrudException if something gets wrong.
     * @see Coupon
     * @see CrudException
     */
    ArrayList<Coupon> getCustomerCoupons(long customerId) throws CrudException;

    /**
     * Gets all coupons of given customer filtered by given category
     *
     * @param customerId The customer id in the database
     * @param category   A Category (enum) refers the category column in the database
     * @return ArrayList of all customer's coupons filtered by category
     */
    ArrayList<Coupon> getCustomerCoupons(long customerId, Category category) throws CrudException;

    /**
     * Gets all coupons of the given customer up to the given maximum price
     *
     * @param customerId The customer id in the database
     * @param maxPrice   Maximum price to filter the result
     * @return ArrayList of all customer's coupons up to maxPrice
     * @throws CrudException if something gets wrong.
     */
    ArrayList<Coupon> getCustomerCoupons(long customerId, double maxPrice) throws CrudException;
}