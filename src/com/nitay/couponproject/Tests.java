package com.nitay.couponproject;

import com.nitay.couponproject.dal.CouponsDBDAO;
import com.nitay.couponproject.enums.ClientType;
import com.nitay.couponproject.exceptions.CouponTitleExistException;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.UpdateException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.facades.AdminFacade;
import com.nitay.couponproject.facades.CompanyFacade;
import com.nitay.couponproject.facades.CustomerFacade;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;
import com.nitay.couponproject.utils.LoginManager;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.nitay.couponproject.utils.MyScanner.*;

public class Tests {
    public static Company createCompany() {
        System.out.println("Create new company");
        return new Company(
                getStringInput("Please enter company name: "),
                getStringInput("Please enter company email: "),
                getStringInput("Please enter company password: ")
        );
    }

    public static Customer createCustomer() {
        System.out.println("Create new customer");
        return new Customer(
                getStringInput("Please enter first name: "),
                getStringInput("Please enter last name: "),
                getStringInput("Please enter email: "),
                getStringInput("Please enter customer password: ")
        );
    }

    public static Coupon createCoupon() {
        System.out.println("Create new coupon");
        return new Coupon(
                getCategory("Enter category name"),
                getStringInput("Enter coupon title"),
                getStringInput("Enter coupon description"),
                Date.valueOf(LocalDate.of(
                        getIntInput("enter start year"),
                        getIntInput("enter start month"),
                        getIntInput("enter start day of month")
                )),
                Date.valueOf(LocalDate.of(
                        getIntInput("enter end year"),
                        getIntInput("enter end month"),
                        getIntInput("enter end day of month")
                )),
                getIntInput("please enter amount"),
                (double) getIntInput("please enter amount"),
                getStringInput("Enter coupon image url")
        );
    }

    public static void testAll() {
//        new Thread(new CouponExpirationDailyJob(CouponsDBDAO.instance)).start();
        try {
            customerTests();
            companyTests();
            adminTests();
        } catch (WrongCredentialsException | CrudException e) {
            e.printStackTrace();
        }
    }

    private static void adminTests() throws WrongCredentialsException, CrudException {
        //ADMIN TESTS
        System.out.println("ADMIN TESTS");
        //Login
        AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
        //New company
        Company company = createCompany();
        long newCompanyId = adminFacade.addNewCompany(company);
        company.setId(newCompanyId);
        System.out.println("New company id = " + newCompanyId);
//            //New customer
        Customer customer = createCustomer();
        long newCustomerId = adminFacade.addNewCustomer(customer);
        //Get all companies
        ArrayList<Company> allCompanies = adminFacade.getAllCompanies();
        System.out.println(allCompanies.toString());
        // Get all customers
        ArrayList<Customer> allCustomers = adminFacade.getAllCustomers();
        System.out.println(allCustomers.toString());
//            Update company

//            update name -> uncomment to get UpdateNameException
//            company.setName(getStringInput("Enter new company name to update: "));

        company.setEmail(getStringInput("Enter new company email to update: "));
        adminFacade.updateCompany(company);
        System.out.println("Updated company details: ");
        System.out.println(adminFacade.getOneCompany((int) newCompanyId));
//            Update customer
        customer.setFirstName(getStringInput("Enter new customer name to update: "));
        adminFacade.updateCustomer(customer);
        System.out.println("Updated customer details: ");
        System.out.println(adminFacade.getOneCustomer((int) customer.getId()));
//            // Delete one company
        adminFacade.deleteCompany((int) newCompanyId);
        System.out.println("deleted company id: " + newCompanyId);
//            // Delete one customer
            adminFacade.deleteCustomer((int) newCustomerId);
            System.out.println("deleted customer id: " + newCustomerId);
    }

    private static void companyTests() {
        try {
            System.out.println("COMPANY TESTS");
            System.out.println("Login company");
            CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login(
                    getStringInput("Please enter company mail: "),
                    getStringInput("Please enter company password: "),
                    ClientType.Company
            );
            //Get company details
            System.out.println("Logged in");
            System.out.println("Company details");
            System.out.println(companyFacade.loggedInCompanyDetails().toString());

            //Add coupon
            System.out.println("Add coupon");
            Coupon newCoupon = createCoupon();
            long newCouponId = companyFacade.addCoupon(newCoupon);
            newCoupon.setId((int) newCouponId);
            System.out.println("coupon with id: " + newCouponId + " added");

            //Update coupon
            System.out.println("Update coupon");
            newCoupon.setTitle(getStringInput("Enter new coupon title to update"));
            companyFacade.updateCoupon(newCoupon);
            System.out.println("coupon with id: " + newCouponId + " updated");

            //Get coupons methods
            System.out.println("Get coupons methods");
            String companyName = companyFacade.loggedInCompanyDetails().getName();
            //All coupons
            System.out.println("All " + companyName + " coupons");
            System.out.println(companyFacade.getCompanyCoupons().toString());
            //By category
            System.out.println("All " + companyName + " coupons by category");
            Category category = getCategory("Enter category name");
            System.out.println(companyFacade.getCompanyCoupons(category));
            //By max price
            System.out.println("All " + companyName + " coupons by max price");
            double maxPrice = getDoubleInput("Enter max price");
            System.out.println(companyFacade.getCompanyCoupons(maxPrice));

            //Delete coupon
            companyFacade.deleteCoupon((int) newCouponId);
            System.out.println("coupon with id: " + newCouponId + " deleted");
        } catch (CrudException |
                WrongCredentialsException |
                CouponTitleExistException |
                UpdateException |
                NullPointerException e) {
            e.printStackTrace();
        }
    }

    private static void customerTests() {
        try {
            System.out.println("CUSTOMER TESTS");
            System.out.println("Login");
            CustomerFacade customerFacade = (CustomerFacade) LoginManager
                    .getInstance()
                    .login(
                            getStringInput("Please enter customer email"),
                            getStringInput("Please enter customer password"),
                            ClientType.Customer
                    );
            System.out.println("Logged in, customer details: ");
            System.out.println(customerFacade.getCustomerDetails().toString());

            //Purchase Coupon
            ArrayList<Coupon> availableCoupons = CouponsDBDAO.instance.getAllCoupons();
            System.out.println("available coupons: ");
            System.out.println(availableCoupons.toString());
            Integer chosenCouponId = getIntInput("Please choose coupon id to purchase");
            Coupon chosenCoupon = CouponsDBDAO.instance.getOneCoupon(chosenCouponId);
            customerFacade.purchaseCoupon(chosenCoupon);

            //Get customer coupons methods

            //Get all customer coupons
            System.out.println("All customer coupons");
            System.out.println(customerFacade.getCustomerCoupons());

            //Get customer coupons by category
            System.out.println("Customer coupons by category");
            Category category = getCategory("Please enter category name: ");
            System.out.println(customerFacade.getCustomerCoupons(category));

            //Get customer coupons by category
            System.out.println("Customer coupons by max price");
            double maxPrice = getDoubleInput("Please enter max price: ");
            System.out.println(customerFacade.getCustomerCoupons(maxPrice));
        } catch (WrongCredentialsException | CrudException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testAll();
    }
}