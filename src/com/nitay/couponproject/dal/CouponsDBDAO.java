package com.nitay.couponproject.dal;

import com.nitay.couponproject.dal.interfaces.CouponsDAO;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.utils.JDBCUtil;
import com.nitay.couponproject.utils.ObjectExtractionUtil;

import java.sql.*;
import java.util.ArrayList;

public class CouponsDBDAO implements CouponsDAO {
    public static final CouponsDBDAO instance = new CouponsDBDAO();
//TODO: Custom exceptions
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
            String sqlStatement = "INSERT INTO coupons (company_id, category, title, description, start_date, end_date, amount, price, image) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, coupon.getCompanyID());
            prep.setString(2, coupon.getCategory().toString());
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
            String sqlStatement = "UPDATE coupons SET company_id = ?, category =?, title = ?, description = ?, start_date = ?, end_date = ?, amount = ?, price = ?, image = ? WHERE id = ? ";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, coupon.getCompanyID());
            prep.setString(2, coupon.getCategory().toString());
            prep.setString(3, coupon.getTitle());
            prep.setString(4, coupon.getDescription());
            prep.setDate(5, coupon.getStartDate());
            prep.setDate(6, coupon.getEndDate());
            prep.setInt(7, coupon.getAmount());
            prep.setDouble(8, coupon.getPrice());
            prep.setString(9, coupon.getImage());
            prep.setLong(10,coupon.getId());
            prep.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add new coupon");
        }
    }

    @Override
    public void deleteCoupon(int couponID) {
        try {
            String sqlStatement = "DELETE FROM coupons WHERE id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, couponID);
            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete coupon with id: " + couponID);
        }
    }

    @Override
    public void deleteCompanyCoupons(int companyID) {
        try {
            String sqlStatement = "DELETE FROM coupons WHERE company_id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, companyID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete company with id: " + companyID + " coupons");
        }
    }

    @Override
    public ArrayList<Coupon> getAllCoupons() {
        try {
            String sqlStatement = "SELECT * FROM coupons";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
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
            prep.setInt(1, couponID);
            ResultSet result = prep.executeQuery();
            if (!result.next()) {
                return null;
            }
            return ObjectExtractionUtil.resultToCoupon(result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve coupon with id: " + couponID);
        }
    }

    public ArrayList<Coupon> getCompanyCoupons(long companyId) {
        try {
            String sqlStatement = "SELECT * FROM coupons WHERE company_id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, companyId);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }

    public ArrayList<Coupon> getCompanyCoupons(long companyId, Category category) {
        try {
            String sqlStatement = "SELECT * FROM coupons WHERE company_id = ? AND category = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, companyId);
            prep.setString(2, String.valueOf(category));
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }

    public ArrayList<Coupon> getCompanyCoupons(long companyId, double maxPrice) {
        try {
            String sqlStatement = "SELECT * FROM coupons WHERE company_id = ? AND price < ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, companyId);
            prep.setDouble(2, maxPrice);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }


    public ArrayList<Coupon> getCustomerCoupons(long customerId) {
        try {
            String sqlStatement = "SELECT * FROM coupons JOIN customer_coupon ON customer_id = ? AND coupon_id=id";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, customerId);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all coupons of customer id: " + customerId);
        }
    }

    public ArrayList<Coupon> getCustomerCoupons(long customerId, Category category) {
        try {
            String sqlStatement = "SELECT * FROM coupons INNER JOIN customer_coupon ON id = ? AND category = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, customerId);
            prep.setString(2, String.valueOf(category));
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all coupons of customer id: " + customerId);
        }
    }

    public ArrayList<Coupon> getCustomerCoupons(long customerId, double maxPrice) {
        try {
            String sqlStatement = "SELECT * FROM coupons INNER JOIN customer_coupon ON id = ? AND price = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, customerId);
            prep.setDouble(2, maxPrice);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all coupons of customer id: " + customerId);
        }
    }



    @Override
    public long addCouponPurchase(long customerId, int couponId) {
        try {
            String sqlStatement = "INSERT INTO customer_coupon (customer_id, coupon_id) VALUES(?, ?)";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, customerId);
            prep.setInt(2, couponId);
            prep.executeUpdate();
            return couponId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add new coupon purchase");
        }

    }

    @Override
    public void deleteCouponPurchase(int couponId) {
        try {
            String sqlStatement = "DELETE FROM customer_coupon WHERE coupon_id = ? ";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, couponId);
            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete coupon " + "(id:" + couponId + ") purchase)");
        }
    }

    @Override
    public void deleteCustomerPurchase(long customerId) {
        try {
            String sqlStatement = "DELETE FROM customer_coupon WHERE customer_id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, customerId);
            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete coupon purchase of customer id: " + customerId);
        }
    }

    @Override
    public int getCategoryId(Category category) {
        try {
            String sqlStatement = "SELECT id FROM categories WHERE name = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, String.valueOf(category));
            ResultSet result = prep.executeQuery();
            if (!result.next()) {
                throw new RuntimeException("Failed to retrieve category id");
            }
            return result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve " + category + " id");
        }
    }
}
