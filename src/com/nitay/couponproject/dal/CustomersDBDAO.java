package com.nitay.couponproject.dal;

import com.nitay.couponproject.dal.interfaces.CustomersDAO;
import com.nitay.couponproject.enums.CrudType;
import com.nitay.couponproject.enums.EntityType;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.facades.ClientFacade;
import com.nitay.couponproject.model.Customer;
import com.nitay.couponproject.utils.ConnectionPool;
import com.nitay.couponproject.utils.ObjectExtractionUtil;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;

/**
 * A singleton data access object, Implements CustomersDAO to make CRUD operations on a SQL database.
 * CustomersDBDAO is connected to the ConnectionPool and used in the facades.
 *
 * @see CustomersDAO
 * @see ConnectionPool
 * @see ClientFacade
 */
public class CustomersDBDAO implements CustomersDAO {
    @Getter
    private static final CustomersDBDAO instance = new CustomersDBDAO();

    ConnectionPool connectionPool;
    private CustomersDBDAO() {
        try {
            connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection with database was failed");
        }
    }

    /**
     * @param email    Customer email
     * @param password Customer password
     * @return Boolean (true if customer is exists in the database)
     * @deprecated
     */
    @Override
    public boolean isCustomerExist(String email, String password) throws CrudException {
        ArrayList<Customer> customers = getAllCustomers();
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(String.valueOf(password.hashCode()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new customer to the database
     *
     * @param customer A Customer object (no Id required)
     * @return Auto generated customer ID
     * @throws CrudException if something gets wrong.
     * @see Customer
     * @see CrudException
     */
    @Override
    public long addCustomer(Customer customer) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "INSERT INTO coupons_project.customers (first_name, last_name, email, password) VALUES(?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setLong(4, customer.getPassword().hashCode());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();
            if (!generatedKeysResult.next()) {
                connectionPool.returnConnection(connection);
                throw new RuntimeException("Failed to retrieve customer id");
            }
            connectionPool.returnConnection(connection);
            return generatedKeysResult.getLong(1);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.CUSTOMER, CrudType.CREATE);
        }
    }

    /**
     * Updates an existing customer in the database
     *
     * @param customer full Customer object (Id param is required)
     * @throws CrudException if something gets wrong.
     * @see Customer
     * @see CrudException
     */
    @Override
    public void updateCustomer(Customer customer) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "UPDATE coupons_project.customers set first_name = ?, last_name = ?, email=?, password=? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setLong(4, customer.getPassword().hashCode());
            preparedStatement.setLong(5,customer.getId());
            preparedStatement.executeUpdate();
            connectionPool.returnConnection(connection);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.CUSTOMER, CrudType.UPDATE);
        }
    }

    /**
     * Deletes a customer from the database
     *
     * @param customerID The customer id in the database
     * @throws CrudException if something gets wrong.
     * @see CrudException
     */
    @Override
    public void deleteCustomer(long customerID) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "DELETE FROM coupons_project.customers WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, customerID);
            preparedStatement.executeUpdate();
            connectionPool.returnConnection(connection);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.CUSTOMER, CrudType.DELETE);
        }
    }

    /**
     * Gets all customers that exist in the database
     *
     * @return An ArrayList of all customers
     * @throws CrudException if something gets wrong.
     * @see Customer
     * @see CrudException
     */
    @Override
    public ArrayList<Customer> getAllCustomers() throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.customers";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Customer> customers = new ArrayList();
            while (result.next()) {
                customers.add(ObjectExtractionUtil.resultToCustomer(result));
            }
            connectionPool.returnConnection(connection);
            return customers;
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.CUSTOMER, CrudType.READ_ALL);
        }
    }

    /**
     * Gets a customer from the database by given ID
     *
     * @param customerID The customer id in the database
     * @return A Customer object
     * @throws CrudException if something gets wrong.
     * @see Customer
     * @see CrudException
     */
    @Override
    public Customer getOneCustomer(long customerID) throws CrudException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.customers WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, customerID);
            ResultSet result = preparedStatement.executeQuery();
            if (!result.next()) {
                connectionPool.returnConnection(connection);
                return null;
            }
            connectionPool.returnConnection(connection);
            return ObjectExtractionUtil.resultToCustomer(result);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.CUSTOMER, CrudType.READ, customerID);
        }
    }
}
