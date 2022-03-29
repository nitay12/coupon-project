package com.nitay.couponproject.tests;

import com.nitay.couponproject.enums.ClientType;
import com.nitay.couponproject.exceptions.*;
import com.nitay.couponproject.facades.AdminFacade;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Customer;
import com.nitay.couponproject.utils.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;

import static com.nitay.couponproject.utils.MyScanner.getStringInput;

public class AdminTests {

    /**
     * Test all admin operations
     *
     * @throws WrongCredentialsException for wrong credentials when login
     * @throws CrudException             for any SQL exception
     * @throws NameExistException        on a new customer or company creation if it's name already exist in the database
     * @throws EmailExistException       on a new customer or company creation if it's email already exist in the database
     * @throws UpdateException           if the user tries to update restricted parameter
     */
    public static void allAdminTests() throws WrongCredentialsException, CrudException, NameExistException, EmailExistException, UpdateException, InterruptedException {
        System.out.println("******STARTING ADMIN TESTS******");
        //Login Test
        AdminFacade adminFacade = loginTest();
        //Creates a company object
        Company company = new Company(
                "TestCompany",
                "testcompany@email.com",
                "123456");
        //Add company test
        long companyId = addCompanyTest(adminFacade, company);
        //Get all companies test
        getAllCompaniesTest(adminFacade);
        //Update company test
        updateCompanyNameTest(adminFacade, company, companyId);

        //Creates a customer object
        Customer customer = new Customer(
                "testCustomerName",
                "testCustomerLastName",
                "testCustomer@email.com",
                "123456");
        //Add customer test
        long newCustomerId = addCustomerTest(adminFacade, customer);

        // Get all customers
        getAllCustomersTest(adminFacade);
        //Update customer
        updateCustomerNameTest(adminFacade, customer);
//      Delete company
        deleteCompanyTest(adminFacade, companyId);
//      Delete customer
        deleteCustomerTest(adminFacade, newCustomerId);
    }

    private static void deleteCustomerTest(AdminFacade adminFacade, long customerId) throws CrudException {
        System.out.println("DELETE THE CREATED CUSTOMER");
        switch (getStringInput("Do you want to delete the created customer?", Arrays.asList("y", "n"))) {
            case "y":
                adminFacade.deleteCustomer(customerId);
                System.out.println("deleted customer id: " + customerId);
                break;
            case "n":
                break;
        }
    }

    private static void deleteCompanyTest(AdminFacade adminFacade, long companyId) throws CrudException, InterruptedException {
        System.out.println("DELETE THE CREATED COMPANY");
        switch (getStringInput("Do you want to delete the created company?", Arrays.asList("y", "n"))) {
            case "y":
                adminFacade.deleteCompany(companyId);
                System.out.println("deleted company id: " + companyId);
                break;
            case "n":
                break;
        }
    }

    private static void updateCustomerNameTest(AdminFacade adminFacade, Customer customer) throws CrudException {
        System.out.println("UPDATE THE GENERATED CUSTOMER");
        System.out.println("UPDATE FIRST NAME (name:UpdatedCustomer");
        customer.setFirstName("UpdatedCustomer");
        adminFacade.updateCustomer(customer);
        System.out.println("UPDATED CUSTOMER DETAILS: ");
        System.out.println(adminFacade.getOneCustomer((int) customer.getId()));
    }

    private static void updateCompanyNameTest(AdminFacade adminFacade, Company company, long companyId) throws UpdateException, CrudException, InterruptedException {
        System.out.println("UPDATE THE GENERATED COMPANY");
//            UPDATE NAME uncomment the method below to get UpdateNameException
//            System.out.println("UPDATE NAME"); company.setName(getStringInput("Enter new company name to update: "));
        System.out.println("UPDATE EMAIL (email:updatedCompany@email.com");
        company.setEmail("email:updatedCompany@email.com");
        adminFacade.updateCompany(company);
        System.out.println("UPDATED COMPANY DETAILS: ");
        System.out.println(adminFacade.getOneCompany(companyId));
    }

    private static void getAllCustomersTest(AdminFacade adminFacade) throws CrudException {
        System.out.println("GET ALL CUSTOMERS");
        ArrayList<Customer> allCustomers = adminFacade.getAllCustomers();
        System.out.println(allCustomers.toString());
    }

    private static void getAllCompaniesTest(AdminFacade adminFacade) throws CrudException, InterruptedException {
        System.out.println("GET ALL COMPANIES TEST");
        ArrayList<Company> allCompanies = adminFacade.getAllCompanies();
        System.out.println(allCompanies.toString());
    }

    private static long addCustomerTest(AdminFacade adminFacade, Customer customer) throws EmailExistException, CrudException {
        System.out.println("ADD NEW CUSTOMER TEST");
        long newCustomerId = adminFacade.addNewCustomer(customer);
        customer.setId(newCustomerId);
        System.out.println(customer.getFirstName() + " WAS ADDED TO THE DATABASE");
        System.out.println(customer.getFirstName() + " id= " + newCustomerId);
        return newCustomerId;
    }

    private static long addCompanyTest(AdminFacade adminFacade, Company company) throws NameExistException, EmailExistException, CrudException, InterruptedException {
        System.out.println("ADD NEW COMPANY TEST");
        long newCompanyId = adminFacade.addNewCompany(company);
        company.setId(newCompanyId);
        System.out.println(company.getName() + " WAS ADDED TO THE DATABASE");
        System.out.println(company.getName() + " id= " + newCompanyId);
        System.out.println("ADDED COMPANY DETAILS (retrieved from the DB): ");
        System.out.println(adminFacade.getOneCompany((int) newCompanyId));
//        System.out.println("NameExist & EmailExist Exceptions TEST");
//        adminFacade.addNewCompany(company);
        return newCompanyId;
    }

    private static AdminFacade loginTest() throws WrongCredentialsException, CrudException, InterruptedException {
        System.out.println("ADMIN LOGIN TEST");
        System.out.println("ADMIN LOGIN TEST");
        System.out.println("credentials: email:admin@admin.com, password:admin");
        AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
        System.out.println("ADMIN LOGIN SUCCEED");
//        System.out.println("WrongCredentialsException TEST");
//        System.out.println("credentials: email:notAdmin@admin.com, password:notAdmin");
//        LoginManager.getInstance().login("notAdmin@admin.com", "notAdmin", ClientType.Administrator);
        return adminFacade;
    }
}
