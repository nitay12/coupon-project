package com.nitay.couponproject.dal.interfaces;

import com.nitay.couponproject.model.Customer;

import java.util.ArrayList;

public interface CustomersDAO {
    public boolean isCustomerExist(String email, String password);
    long addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerID);
    ArrayList<Customer> getAllCustomers();
    Customer getOneCustomer(int customerID);
}
