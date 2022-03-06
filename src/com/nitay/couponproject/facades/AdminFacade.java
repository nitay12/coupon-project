package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.EmailExistException;
import com.nitay.couponproject.exceptions.NameExistException;
import com.nitay.couponproject.exceptions.UpdateException;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;
import com.nitay.couponproject.utils.LoginManager;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

/**
 * A facade for all admin operations
 */
@AllArgsConstructor
public class AdminFacade extends ClientFacade {
    public static AdminFacade instance = new AdminFacade();
    //TODO Remove side effect or change login to void

    /**
     * Login validate email and password (currently hard-coded)
     *
     * @param email    Admin's email
     * @param password Admin's password
     * @return Boolean if log in succeeds
     * @see LoginManager
     */
    @Override
    public boolean login(String email, String password) {
        //TODO:Real login implementation
//        Hard coded implementation as mentioned in the instructions:
        String ADMIN_MAIL = "admin@admin.com";
        String ADMIN_PASS = "admin";
        return email.equals(ADMIN_MAIL) && password.equals(ADMIN_PASS);
    }

    /**
     * Checks if a company name already exists in ArrayList of companies
     *
     * @param company   A company object
     * @param companies ArrayList of companies
     * @throws NameExistException If the name already exist
     */
    private void isCompanyNameExist(Company company, ArrayList<Company> companies) throws NameExistException {
        for (Company c :
                companies) {
            if (company.getName().equals(c.getName())) {
                throw new NameExistException("Sorry this name already exist");
            }
        }
    }

    //TODO: generify the isEmailExist method

    /**
     * Checks if a company email already exists in ArrayList of companies
     *
     * @param company   A company object
     * @param companies ArrayList of companies
     * @throws EmailExistException If the email already exist
     */
    private void isCompanyEmailExist(Company company, ArrayList<Company> companies) throws EmailExistException {
        for (Company c :
                companies) {
            if (company.getEmail().equals(c.getEmail())) {
                throw new EmailExistException("Sorry this email already exist");
            }
        }
    }

    /**
     * Checks if a customer email already exists in ArrayList of companies
     *
     * @param customer     A Customer object
     * @param allCustomers ArrayList of customers
     * @throws EmailExistException If the email already exist
     */
    private void isCustomerEmailExist(Customer customer, ArrayList<Customer> allCustomers) throws EmailExistException {
        for (Customer c : allCustomers) {
            if (customer.getEmail().equalsIgnoreCase(c.getEmail())) {
                throw new EmailExistException("This email already exist");
            }
        }
    }

    /**
     * Adds a new company to the database only if the email and the name are not exist in the database
     *
     * @param company A company object (id param is not required)
     * @return The generated company id
     * @throws NameExistException  If the company's name already exist in the database
     * @throws EmailExistException If the company's email already exist in the database
     * @throws CrudException       If there is any SQL exception
     */
    public long addNewCompany(Company company) throws NameExistException, EmailExistException, CrudException {
        ArrayList<Company> allCompanies = companiesDAO.getAllCompanies();
        isCompanyNameExist(company, allCompanies);
        isCompanyEmailExist(company, allCompanies);
        return companiesDAO.addCompany(company);
    }

    /**
     * Updates an existing company in the database (company name cannot be updated)
     *
     * @param company A company object (id param required)
     * @throws UpdateException If the user trying to update the company name
     * @throws CrudException   If there is any SQL exception
     */
    public void updateCompany(Company company) throws UpdateException, CrudException {
        Company companyToUpdate = companiesDAO.getOneCompany(company.getId());
        if (!company.getName().equals(companyToUpdate.getName())) {
            throw new UpdateException("Name cannot be updated");
        } else {
            companiesDAO.updateCompany(company);
        }
    }

    /**
     * Deletes a company and all it's coupons and coupon-purchases from the database
     *
     * @param companyId The company id in the database
     * @throws CrudException If there is any SQL exception
     */
    public void deleteCompany(int companyId) throws CrudException {
        ArrayList<Coupon> coupons = couponsDAO.getAllCoupons();
        for (Coupon coupon :
                coupons) {
            if (coupon.getCompanyID() == companyId) {
                couponsDAO.deletePurchaseByCouponId(coupon.getId());
            }
        }
        couponsDAO.deleteCompanyCoupons(companyId);
        companiesDAO.deleteCompany(companyId);
    }

    /**
     * Gets all the companies that exist in the database
     * Sets the coupons param for all the companies
     *
     * @return ArrayList of all the companies
     * @throws CrudException If there is any SQL exception
     * @see Company
     */
    public ArrayList<Company> getAllCompanies() throws CrudException {
        ArrayList<Company> allCompanies = companiesDAO.getAllCompanies();
        for (Company company :
                allCompanies) {
            company.setCoupons(couponsDAO.getCompanyCoupons(company.getId()));
        }
        return allCompanies;
    }

    /**
     * Gets one company by given id
     *
     * @param companyId The company id in the database
     * @return Company object
     * @throws CrudException If there is any SQL exception
     * @see Company
     */
    public Company getOneCompany(int companyId) throws CrudException {
        Company company = companiesDAO.getOneCompany(companyId);
        company.setCoupons(couponsDAO.getCompanyCoupons(companyId));
        return company;
    }

    /**
     * Adds a new customer to the database
     * Checks if the customer email already exist
     *
     * @param customer Customer object (id param is not required)
     * @return The generated customer id
     * @throws EmailExistException If the customer email already exist
     * @throws CrudException       If there is any SQL exception
     * @see Customer
     */
    public long addNewCustomer(Customer customer) throws EmailExistException, CrudException {
        ArrayList<Customer> allCustomers = customersDAO.getAllCustomers();
        isCustomerEmailExist(customer, allCustomers);
        return customersDAO.addCustomer(customer);
    }

    /**
     * Updates an existing customer in the database
     *
     * @param customer A Customer object (param id is required)
     * @throws CrudException If there is any SQL exception
     * @see Customer
     */
    public void updateCustomer(Customer customer) throws CrudException {
        customersDAO.updateCustomer(customer);
    }

    /**
     * Deletes an existing customer and all it's coupons and coupon-purchases from the database
     *
     * @param customerId The customer id in the database
     * @throws CrudException If there is any SQL exception
     */
    public void deleteCustomer(int customerId) throws CrudException {
        couponsDAO.deletePurchaseByCustomerId(customerId);
        customersDAO.deleteCustomer(customerId);
    }

    /**
     * Gets all the customers in the database
     *
     * @return ArrayList of all the customers
     * @throws CrudException If there is any SQL exception
     */
    public ArrayList<Customer> getAllCustomers() throws CrudException {
        ArrayList<Customer> allCustomers = customersDAO.getAllCustomers();
        for (Customer customer :
                allCustomers) {
            customer.setCoupons(couponsDAO.getCustomerCoupons(customer.getId()));
        }
        return allCustomers;
    }

    /**
     * Gets one customer from the database
     *
     * @param customerId The customer id in the database
     * @return A Customer object
     * @throws CrudException If there is any SQL exception
     */
    public Customer getOneCustomer(int customerId) throws CrudException {
        Customer customer = customersDAO.getOneCustomer(customerId);
        customer.setCoupons(couponsDAO.getCustomerCoupons(customer.getId()));
        return customer;
    }
}