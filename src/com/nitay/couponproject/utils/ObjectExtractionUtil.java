package com.nitay.couponproject.utils;

import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectExtractionUtil {
    public static Company resultToCompany(ResultSet result) throws SQLException {
        return new Company(
                result.getLong("id"), result.getString("name"), result.getString("email"), String.valueOf(result.getLong("password")
        ));
    }

    public static Customer resultToCustomer(ResultSet result) throws SQLException {
        return new Customer(
                result.getLong("id"),
                result.getString("first_name"),
                result.getString("last_name"),
                result.getString("email"),
                result.getString("password")
        );
    }

    public static Coupon resultToCoupon(ResultSet result) throws SQLException {
        return new Coupon(
                result.getInt("id"),
                result.getInt("company_id"),
                Category.values()[result.getInt("category_id")],
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
