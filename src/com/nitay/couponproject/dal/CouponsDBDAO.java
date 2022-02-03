package com.nitay.couponproject.dal;

import com.nitay.couponproject.dal.interfaces.CouponsDAO;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.utils.JDBCUtil;
import com.nitay.couponproject.utils.ObjectExtractionUtil;

import java.sql.*;
import java.util.ArrayList;

public class CouponsDBDAO implements CouponsDAO {
    public static final CouponsDBDAO instance = new CouponsDBDAO();

    private CouponsDBDAO() {
        try {
            connection = JDBCUtil.getConnection();
        } catch (
                SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection with database was failed");
        }
    }

    private final Connection connection;

    @Override
    public long addCoupon(Coupon coupon) {
        try {
            String sqlStatement = "INSERT INTO coupons (company_id, category_id, title, description, start_date, end_date, amount, price, image) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, coupon.getCompanyID());
            prep.setInt(2, coupon.getCategoryID());
            prep.setString(3, coupon.getTitle());
            prep.setString(4, coupon.getDescription());
            prep.setDate(5, coupon.getStartDate());
            prep.setDate(6, coupon.getEndDate());
            prep.setInt(7, coupon.getAmount());
            prep.setDouble(8, coupon.getPrice());
            prep.setString(9, coupon.getImage());
            prep.executeUpdate();
            ResultSet result = prep.getGeneratedKeys();

            if (!result.next()) {
                throw new RuntimeException("Failed to retrieve coupon id");
            }
            return result.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add new coupon");
        }
    }

    @Override
    public void updateCoupon(Coupon coupon) {
        try {
            String sqlStatement = "UPDATE coupons SET (company_id = ?, category_id =?, title = ?, description = ?, start_date = ?, end_date = ?, amount = ?, price = ?, image = ?) WHERE id = ? ";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, coupon.getCompanyID());
            prep.setInt(2, coupon.getCategoryID());
            prep.setString(3, coupon.getTitle());
            prep.setString(4, coupon.getDescription());
            prep.setDate(5, coupon.getStartDate());
            prep.setDate(6, coupon.getEndDate());
            prep.setInt(7, coupon.getAmount());
            prep.setDouble(8, coupon.getPrice());
            prep.setString(9, coupon.getImage());
            prep.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add new coupon");
        }
    }

    @Override
    public void deleteCoupon(int couponID) {
        try {
            String sqlStatement = "DELETE FROM customers WHERE id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, couponID);
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to delete coupon with id: "+couponID);
        }

    }

    @Override
    public ArrayList<Coupon> getAllCoupons() {
        try {
        String sqlStatement = "SELECT * FROM coupons";
            PreparedStatement prep = connection.prepareStatement(sqlStatement,Statement.RETURN_GENERATED_KEYS);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()){
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all coupons");
        }
    }

    @Override
    public Coupon getOneCoupon(int couponID) {
        try {
            String sqlStatement = "SELECT * FROM coupons WHERE id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1,couponID);
            ResultSet result = prep.executeQuery();
            if(!result.next()){
                return null;
            }
            return ObjectExtractionUtil.resultToCoupon(result);
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve coupon with id: "+ couponID);
        }
    }

    @Override
    public void addCouponPurchase(int customerId, int couponId) {

    }

    @Override
    public void deleteCouponPurchase(int customerId, int couponId) {

    }
}
