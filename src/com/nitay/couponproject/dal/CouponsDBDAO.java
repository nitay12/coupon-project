package com.nitay.couponproject.dal;

import com.nitay.couponproject.dal.interfaces.CouponsDAO;
import com.nitay.couponproject.enums.CrudType;
import com.nitay.couponproject.enums.EntityType;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.facades.ClientFacade;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.utils.ConnectionPool;
import com.nitay.couponproject.utils.ObjectExtractionUtil;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;

/**
 * A singleton data access object, Implements CouponsDAO interface to make CRUD operations on a SQL database.
 * CouponsDBDAO is connected to the ConnectionPool and used in the facades.
 *
 * @see CouponsDAO
 * @see ConnectionPool
 * @see ClientFacade
 */
public class CouponsDBDAO implements CouponsDAO {
    @Getter
    private static final CouponsDBDAO instance = new CouponsDBDAO();
    private ConnectionPool connectionPool;

    private CouponsDBDAO() {
        try {
            this.connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection with the database failed");
        }
    }

    /**
     * Adds a new coupon to the database
     *
     * @param coupon A Coupon object (no Id required)
     * @return Auto generated coupon ID
     * @throws CrudException if something gets wrong.
     * @see Coupon
     * @see CrudException
     */
    @Override
    public long addCoupon(Coupon coupon) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "INSERT INTO coupons_project.coupons (company_id, category, title, description, start_date, end_date, amount, price, image) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            connectionPool.returnConnection(connection);
            return result.getLong(1);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.CREATE);
        }
    }

    /**
     * Updates an existing coupon in the database
     *
     * @param coupon A full Coupon object (Id param is required)
     * @throws CrudException if something gets wrong.
     * @see Coupon
     * @see CrudException
     */
    @Override
    public void updateCoupon(Coupon coupon) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "UPDATE coupons_project.coupons SET company_id = ?, category =?, title = ?, description = ?, start_date = ?, end_date = ?, amount = ?, price = ?, image = ? WHERE id = ? ";
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
            connectionPool.returnConnection(connection);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.UPDATE);
        }
    }

    /**
     * Deletes a coupon from the database
     *
     * @param couponID The coupon id in the database
     * @throws CrudException if something gets wrong.
     * @see CrudException
     */
    @Override
    public void deleteCoupon(long couponID) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "DELETE FROM coupons_project.coupons WHERE id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, couponID);
            prep.executeUpdate();
            connectionPool.returnConnection(connection);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.DELETE, couponID);
        }
    }

    /**
     * Deletes all company's coupons
     *
     * @param companyID The company id
     * @throws CrudException if something gets wrong.
     * @see CrudException
     */
    @Override
    public void deleteCompanyCoupons(long companyID) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "DELETE FROM coupons_project.coupons WHERE company_id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, companyID);
            connectionPool.returnConnection(connection);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException("Failed to delete all coupons of company with id: " + companyID);
        }
    }

    /**
     * Gets all coupons that exist in the database
     *
     * @return An ArrayList of all coupons
     * @throws CrudException if something gets wrong.
     * @see Coupon
     * @see CrudException
     */
    @Override
    public ArrayList<Coupon> getAllCoupons() throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.coupons";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            connectionPool.returnConnection(connection);
            return coupons;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.READ_ALL);
        }
    }

    /**
     * Gets a coupon from the database by given ID
     *
     * @param couponID The coupon id in the database
     * @return A Coupon object
     * @throws CrudException if something gets wrong
     * @see Coupon
     * @see CrudException
     */
    @Override
    public Coupon getOneCoupon(long couponID) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.coupons WHERE id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, couponID);
            ResultSet result = prep.executeQuery();
            if (!result.next()) {
                connectionPool.returnConnection(connection);
                return null;
            }
            connectionPool.returnConnection(connection);
            return ObjectExtractionUtil.resultToCoupon(result);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.READ, couponID);
        }
    }

    /**
     * Gets all coupons of given company
     *
     * @param companyId The company id in the database
     * @return ArrayList of all company's coupons
     */
    @Override
    public ArrayList<Coupon> getCompanyCoupons(long companyId) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.coupons WHERE company_id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, companyId);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            connectionPool.returnConnection(connection);
            return coupons;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }

    /**
     * Gets all the given company's coupons filtered by given category
     *
     * @param companyId The company id in the database
     * @param category  A Category (enum) refers the category column in the database
     * @return ArrayList of all company's coupons filtered by category
     */
    @Override
    public ArrayList<Coupon> getCompanyCoupons(long companyId, Category category) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.coupons WHERE company_id = ? AND category = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, companyId);
            prep.setString(2, category.name());
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }

    /**
     * Gets all coupons of the given company up to the given maximum price
     *
     * @param companyId The company id in the database
     * @param maxPrice  Maximum price to filter the result
     * @return ArrayList of all company's coupons up to maxPrice
     * @throws CrudException if something gets wrong
     */
    @Override
    public ArrayList<Coupon> getCompanyCoupons(long companyId, double maxPrice) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.coupons WHERE company_id = ? AND price < ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, companyId);
            prep.setDouble(2, maxPrice);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            connectionPool.returnConnection(connection);
            return coupons;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException("Failed to retrieve all coupons of company id: " + companyId);
        }
    }

    /**
     * Gets all coupons of given customer
     *
     * @param customerId The customer id in the database
     * @return An ArrayList of all given customer's coupons
     * @throws CrudException if something gets wrong.
     * @see Coupon
     * @see CrudException
     */
    @Override
    public ArrayList<Coupon> getCustomerCoupons(long customerId) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.coupons JOIN coupons_project.customer_coupon ON customer_id = ? AND coupon_id=id";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, customerId);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            connectionPool.returnConnection(connection);
            return coupons;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException("Failed to retrieve all coupons of customer id: " + customerId);
        }
    }

    /**
     * Gets all coupons of given customer filtered by given category
     *
     * @param customerId The customer id in the database
     * @param category   A Category (enum) refers the category column in the database
     * @return ArrayList of all customer's coupons filtered by category
     */
    @Override
    public ArrayList<Coupon> getCustomerCoupons(long customerId, Category category) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.coupons JOIN coupons_project.customer_coupon ON customer_id = ? AND coupon_id = id AND category = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, customerId);
            prep.setString(2, category.name());
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            connectionPool.returnConnection(connection);
            return coupons;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON, CrudType.READ_ALL, (int) customerId);
        }
    }

    /**
     * Gets all coupons of the given customer up to the given maximum price
     *
     * @param customerId The customer id in the database
     * @param maxPrice   Maximum price to filter the result
     * @return ArrayList of all customer's coupons up to maxPrice
     * @throws CrudException if something gets wrong.
     */
    @Override
    public ArrayList<Coupon> getCustomerCoupons(long customerId, double maxPrice) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.coupons JOIN coupons_project.customer_coupon ON customer_id = ? AND coupon_id=id AND price < ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement);
            prep.setLong(1, customerId);
            prep.setDouble(2, maxPrice);
            ResultSet result = prep.executeQuery();
            ArrayList<Coupon> coupons = new ArrayList<>();
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            connectionPool.returnConnection(connection);
            return coupons;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException("Failed to retrieve all coupons of customer id: " + customerId);
        }
    }

    /**
     * Add a one coupon purchase by one customer
     *
     * @param customerId The customer id (must exist in the database)
     * @param couponId   The coupon id (must exist in the database)
     * @return The Coupon id
     * @throws CrudException if something gets wrong
     * @see CrudException
     */
    @Override
    public long addCouponPurchase(long customerId, long couponId) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "INSERT INTO coupons_project.customer_coupon (customer_id, coupon_id) VALUES(?, ?)";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, customerId);
            prep.setLong(2, couponId);
            prep.executeUpdate();
            connectionPool.returnConnection(connection);
            return couponId;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON_PURCHASE, CrudType.CREATE);
        }

    }

    //I separated the delete purchase method into two methods for better implementation

    /**
     * Deletes a coupon purchase
     *
     * @param couponId The coupon id
     * @throws CrudException if something gets wrong.
     */
    @Override
    public void deletePurchaseByCouponId(long couponId) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "DELETE FROM coupons_project.customer_coupon WHERE coupon_id = ? ";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, couponId);
            prep.executeUpdate();
            connectionPool.returnConnection(connection);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COUPON_PURCHASE, CrudType.DELETE, couponId);
        }
    }

    /**
     * Deletes a coupon purchase
     *
     * @param customerId The customer id
     * @throws CrudException if something gets wrong
     */
    @Override
    public void deletePurchaseByCustomerId(long customerId) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "DELETE FROM coupons_project.customer_coupon WHERE customer_id = ?";
            PreparedStatement prep = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, customerId);
            prep.executeUpdate();
            connectionPool.returnConnection(connection);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException("Failed to delete coupon purchase of customer id: " + customerId);
        }
    }
}