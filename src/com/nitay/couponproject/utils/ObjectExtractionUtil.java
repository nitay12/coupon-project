package com.nitay.couponproject.utils;

import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An object extraction utility class. All it's methods are static, gets a Result set and return the appropriate object.
 */
public class ObjectExtractionUtil {
    /**
     * A Company extraction method
     *
     * @param result A ResultSet from the companies table and returns a new Company object
     * @return Company object
     * @throws SQLException If there is any SQL exception
     * @see Company
     */
    public static Company resultToCompany(ResultSet result) throws SQLException {
        return new Company(
                result.getLong("id"),
                result.getString("name"),
                result.getString("email"),
                String.valueOf(result.getLong("password")
                ));
    }

    /**
     * A Customer extraction method
     *
     * @param result A ResultSet from the customers table and returns a new Customer object
     * @return Customer object
     * @throws SQLException If there is any SQL exception
     * @see Customer
     */
    public static Customer resultToCustomer(ResultSet result) throws SQLException {
        return new Customer(
                result.getLong("id"),
                result.getString("first_name"),
                result.getString("last_name"),
                result.getString("email"),
                result.getString("password")
        );
    }

    /**
     * A Coupon extraction method
     *
     * @param result A ResultSet from the coupons table and returns a new Coupon object
     * @return Coupon object
     * @throws SQLException If there is any SQL exception
     * @see Coupon
     */
    public static Coupon resultToCoupon(ResultSet result) throws SQLException {
        return new Coupon(
                result.getInt("id"),
                result.getInt("company_id"),
                Category.valueOf(result.getString("category")),
                result.getString("title"),
                result.getString("description"),
                result.getDate("start_date"),
                result.getDate("end_date"),
                result.getInt("amount"),
                result.getDouble("price"),
                result.getString("image")
        );
    }
}
