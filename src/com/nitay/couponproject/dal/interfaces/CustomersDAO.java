package com.nitay.couponproject.dal.interfaces;

import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.model.Customer;

import java.util.ArrayList;

/**
 * An interface for customers data access object
 */
public interface CustomersDAO {

    /**
     * @param email    Customer email
     * @param password Customer password
     * @return Boolean (true if customer is exists in the database)
     * @deprecated
     */
    boolean isCustomerExist(String email, String password) throws CrudException;

    /**
     * Adds a new customer to the database
     *
     * @param customer A Customer object (no Id required)
     * @return Auto generated customer ID
     * @throws CrudException if something gets wrong.
     * @see Customer
     * @see CrudException
     */
    long addCustomer(Customer customer) throws CrudException;

    /**
     * Updates an existing customer in the database
     *
     * @param customer full Customer object (Id param is required)
     * @throws CrudException if something gets wrong.
     * @see Customer
     * @see CrudException
     */

    void updateCustomer(Customer customer) throws CrudException;

    /**
     * Deletes a customer from the database
     *
     * @param customerID The customer id in the database
     * @throws CrudException if something gets wrong.
     * @see CrudException
     */
    void deleteCustomer(long customerID) throws CrudException;

    /**
     * Gets all customers that exist in the database
     *
     * @return An ArrayList of all customers
     * @throws CrudException if something gets wrong.
     * @see Customer
     * @see CrudException
     */
    ArrayList<Customer> getAllCustomers() throws CrudException;

    /**
     * Gets a customer from the database by given ID
     *
     * @param customerID The customer id in the database
     * @return A Customer object
     * @throws CrudException if something gets wrong.
     * @see Customer
     * @see CrudException
     */
    Customer getOneCustomer(long customerID) throws CrudException;
}
