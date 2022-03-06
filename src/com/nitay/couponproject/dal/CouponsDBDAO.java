package com.nitay.couponproject.dal;

import com.nitay.couponproject.dal.interfaces.CouponsDAO;
import com.nitay.couponproject.enums.CrudType;
import com.nitay.couponproject.enums.EntityType;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.utils.ConnectionPool;
import com.nitay.couponproject.utils.ObjectExtractionUtil;

import java.sql.*;
import java.util.ArrayList;

public class CouponsDBDAO implements CouponsDAO {
    public static final CouponsDBDAO instance = new CouponsDBDAO();
    private CouponsDBDAO() {
        try {
//            connection = JDBCUtil.getConnection();
            connection = ConnectionPool.getInstance().getConnection();
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection with the database failed");
        }
    }

    private final Connection connection;

    @Override
    public long addCoupon(Coupon coupon) throws CrudException {
        try {
            String sqlStatement = "INSERT INTO coupons (company_id, category, title, description, start_date, end_date, amount, price, image) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, coupon.getCompanyID());
            prep.setString(2, coupon.getCategory().name());
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
                throw new CrudException("Failed to retrieve coupon id");
            }
            return result.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.CREATE);
        }
    }

    @Override
    public void updateCoupon(Coupon coupon) throws CrudException {
        try {
            String sqlStatement = "UPDATE coupons SET company_id = ?, category =?, title = ?, description = ?, start_date = ?, end_date = ?, amount = ?, price = ?, image = ? WHERE id = ? ";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, coupon.getCompanyID());
            prep.setString(2, coupon.getCategory().name());
            prep.setString(3, coupon.getTitle());
            prep.setString(4, coupon.getDescription());
            prep.setDate(5, coupon.getStartDate());
            prep.setDate(6, coupon.getEndDate());
            prep.setInt(7, coupon.getAmount());
            prep.setDouble(8, coupon.getPrice());
            prep.setString(9, coupon.getImage());
            prep.setLong(10, coupon.getId());
            prep.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.UPDATE);
        }
    }

    @Override
    public void deleteCoupon(int couponID) throws CrudException {
        try {
            String sqlStatement = "DELETE FROM coupons WHERE id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, couponID);
            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.DELETE, couponID);
        }
    }

    @Override
    public void deleteCompanyCoupons(int companyID) throws CrudException {
        try {
            String sqlStatement = "DELETE FROM coupons WHERE company_id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, companyID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException("Failed to delete all coupons of company with id: " + companyID);
        }
    }

    @Override
    public ArrayList<Coupon> getAllCoupons() throws CrudException {
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
            throw new CrudException(EntityType.COUPON, CrudType.READ_ALL);
        }
    }

    @Override
    public Coupon getOneCoupon(int couponID) throws CrudException {
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
            throw new CrudException(EntityType.COUPON, CrudType.READ, couponID);
        }
    }

    @Override
    public ArrayList<Coupon> getCompanyCoupons(long companyId) throws CrudException {
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
            throw new CrudException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }

    @Override
    public ArrayList<Coupon> getCompanyCoupons(long companyId, Category category) throws CrudException {
        try {
            String sqlStatement = "SELECT * FROM coupons WHERE company_id = ? AND category = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, companyId);
            prep.setString(2, category.name());
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }

    @Override
    public ArrayList<Coupon> getCompanyCoupons(long companyId, double maxPrice) throws CrudException {
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
            throw new CrudException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }


    @Override
    public ArrayList<Coupon> getCustomerCoupons(long customerId) throws CrudException {
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
            throw new CrudException("Failed to retrieve all coupons of customer id: " + customerId);
        }
    }

    @Override
    public ArrayList<Coupon> getCustomerCoupons(long customerId, Category category) throws CrudException {
        try {
            String sqlStatement = "SELECT * FROM coupons JOIN customer_coupon ON customer_id = ? AND coupon_id = id AND category = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, customerId);
            prep.setString(2, category.name());
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.READ_ALL, (int) customerId);
        }
    }

    @Override
    public ArrayList<Coupon> getCustomerCoupons(long customerId, double maxPrice) throws CrudException {
        try {
            String sqlStatement = "SELECT * FROM coupons JOIN customer_coupon ON customer_id = ? AND coupon_id=id AND price < ?";
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
            throw new CrudException("Failed to retrieve all coupons of customer id: " + customerId);
        }
    }



    @Override
    public long addCouponPurchase(long customerId, int couponId) throws CrudException {
        try {
            String sqlStatement = "INSERT INTO customer_coupon (customer_id, coupon_id) VALUES(?, ?)";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, customerId);
            prep.setInt(2, couponId);
            prep.executeUpdate();
            return couponId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON_PURCHASE, CrudType.CREATE);
        }

    }

    @Override
    public void deletePurchaseByCouponId(long couponId) throws CrudException {
        try {
            String sqlStatement = "DELETE FROM customer_coupon WHERE coupon_id = ? ";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, couponId);
            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON_PURCHASE, CrudType.DELETE, couponId);
        }
    }

    @Override
    public void deletePurchaseByCustomerId(long customerId) throws CrudException {
        try {
            String sqlStatement = "DELETE FROM customer_coupon WHERE customer_id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, customerId);
            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException("Failed to delete coupon purchase of customer id: " + customerId);
        }
    }
}