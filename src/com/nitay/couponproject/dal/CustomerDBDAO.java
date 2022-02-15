package com.nitay.couponproject.dal;

import com.nitay.couponproject.dal.interfaces.CustomersDAO;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Customer;
import com.nitay.couponproject.utils.JDBCUtil;
import com.nitay.couponproject.utils.ObjectExtractionUtil;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDBDAO implements CustomersDAO {
    public static final CustomerDBDAO instance = new CustomerDBDAO();

    private CustomerDBDAO() {
        try {
            connection = JDBCUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection with database was failed");
        }
    }

    private final Connection connection;

    public boolean isCustomerExist(String email, String password) {
        ArrayList<Customer> customers = getAllCustomers();
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(String.valueOf(password.hashCode()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long addCustomer(Customer customer) {
        try {
            String sqlStatement = "INSERT INTO customers (first_name, last_name, email, password) VALUES(?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setLong(4, customer.getPassword().hashCode());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new RuntimeException("Failed to retrieve customer id");
            }
            return generatedKeysResult.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a new customer");
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        try {
            String sqlStatement = "UPDATE customers set (first_name = ?, last_name = ?, email=?, password=?) WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setLong(4, customer.getPassword().hashCode());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update a new customer");
        }
    }


    @Override
    public void deleteCustomer(int customerID) {
        try {
            String sqlStatement = "DELETE FROM customers WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, customerID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ArrayList<Customer> getAllCustomers() {
        try {
            String sqlStatement = "SELECT * FROM customers";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Customer> customers = new ArrayList();
            while (result.next()) {
                customers.add(ObjectExtractionUtil.resultToCustomer(result));
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all companies");
        }
    }

    @Override
    public Customer getOneCustomer(int customerID) {
        try{
            String sqlStatement = "SELECT * FROM customers WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,customerID);
            ResultSet result = preparedStatement.executeQuery();
            if(!result.next()){
                return null;
            }
            return ObjectExtractionUtil.resultToCustomer(result);
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("failed to retrieve customer with id: "+customerID);
        }
    }
}
