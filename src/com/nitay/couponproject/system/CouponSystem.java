package com.nitay.couponproject.system;

import com.nitay.couponproject.dal.CouponsDBDAO;
import com.nitay.couponproject.enums.ClientType;
import com.nitay.couponproject.exceptions.*;
import com.nitay.couponproject.facades.AdminFacade;
import com.nitay.couponproject.facades.CompanyFacade;
import com.nitay.couponproject.facades.CustomerFacade;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;
import com.nitay.couponproject.tasks.CouponExpirationDailyJob;
import com.nitay.couponproject.utils.DBInitializer;
import com.nitay.couponproject.utils.LoginManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nitay.couponproject.tests.Tests.testAll;
import static com.nitay.couponproject.utils.MyScanner.*;

public class CouponSystem {
    static final private CouponExpirationDailyJob expirationDailyJob = new CouponExpirationDailyJob(CouponsDBDAO.getInstance());
    static final private Thread expirationJobThread = new Thread(expirationDailyJob);

    public static void main(String[] args) throws URISyntaxException, IOException {
        boolean quit = false;
        switch (
                getStringInput(
                        "Would you like to reinitialize the project database with mock data?)",
                        Arrays.asList("y", "n"))) {
            case "y":
                try {
                    DBInitializer.dropSchema();
                } catch (SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    DBInitializer.setupDatabase();
                } catch (SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    DBInitializer.insertMockData();
                } catch (CrudException | InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "n":
                break;
        }
            expirationJobThread.start();
        while (!quit) {
            printMainMenu();
            String answer = getStringInput("", Arrays.asList("0", "1", "2", "3","DOCS"));
            switch (answer) {
                case "0":
                    expirationDailyJob.setStop(!expirationDailyJob.isStop());
                    break;
                case "1":
                    try {
                        openLoginMenu();
                    } catch (WrongCredentialsException | InterruptedException | CrudException | NameExistException | EmailExistException | UpdateException | CouponTitleExistException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        testAll();
                    } catch (WrongCredentialsException | InterruptedException | CrudException | NameExistException | EmailExistException | UpdateException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    quit = true;
                    expirationJobThread.stop();
                    break;
                case "DOCS":
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI("https://nitay12.github.io/coupon-project/"));
                    }
            }
        }
    }

    private static void openLoginMenu() throws WrongCredentialsException, InterruptedException, CrudException, NameExistException, EmailExistException, UpdateException, CouponTitleExistException {
        String answer = getStringInput("Please choose a client type, press R to return the previous menu", Arrays.asList("admin", "company", "customer", "R"));
        boolean quit = false;
        while (!quit) {
            switch (answer) {
                case "admin":
                    AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login(
                            getStringInput("Enter admin email"),
                            getStringInput("Enter admin password"),
                            ClientType.Administrator
                    );
                    openAdminMenu(adminFacade);
                    break;
                case "company":
                    CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login(
                            getStringInput("Enter company email"),
                            getStringInput("Enter company password"),
                            ClientType.Company
                    );
                    openCompanyMenu(companyFacade);
                    break;
                case "customer":
                    CustomerFacade customerFacade = (CustomerFacade) LoginManager
                            .getInstance()
                            .login(
                                    getStringInput("Enter customer email: (try Customer1@test.com for system test)"),
                                    getStringInput("Enter customer password: (try 123456 for system test)"),
                                    ClientType.Customer
                            );
                    System.out.println("Sorry, the customer menu is not ready");
//                    openCustomerMenu(customerFacade);
                case "R":
                    quit = true;
                    break;
            }
        }
    }

    private static void openCustomerMenu(CustomerFacade customerFacade) {
        boolean quit = false;
        while (!quit) {
            switch (
                    getStringInput("""
                                    Please choose an action from the list:
                                    1. Purchase coupon
                                    2. Print your purchased coupons (with optionally filters)
                                    R. Return to the main menu""",
                            Arrays.asList("1", "2", "R"))
            ) {
                case "1":
                    break;
                case "2":
                    break;
                case "R":
                    quit = true;
            }
        }
    }

    private static void openCompanyMenu(CompanyFacade companyFacade) throws InterruptedException, CrudException, CouponTitleExistException, UpdateException {
        boolean quit = false;
        System.out.println("Welcome " + companyFacade.loggedInCompanyDetails().getName());
        while (!quit) {
            System.out.println("""
                    Press 1 to add a new coupon
                    Press 2 to update a coupon
                    Press 3 to delete a coupon
                    press 4 to print all your company coupons (with optional filters)
                    press R to return to the main menu
                    """);
            String answer = getStringInput("Please choose an action from the menu above", Arrays.asList("1", "2", "3", "4", "R"));
            switch (answer) {
                case "1":
                    final long newCouponId = companyFacade.addCoupon(createCoupon());
                    System.out.println("Coupon with id:" + newCouponId + " was added");
                    break;
                case "2":
                    List<String> availableCouponsId = getCompanyCouponsId(companyFacade);
                    final String couponToUpdateId = getStringInput("Choose coupon id from the list", availableCouponsId);
                    Coupon couponToUpdate = companyFacade.getOneCompanyCoupon(Long.parseLong(couponToUpdateId));
                    if (couponToUpdate != null) {
                        couponToUpdate.setTitle(getStringInput("Enter new coupon title"));
                        companyFacade.updateCoupon(couponToUpdate);
                        System.out.println("Coupon with id" + couponToUpdateId + " was updated");
                    } else System.out.println("Coupon not found");
                    break;
                case "3":
                    final String couponToDelete = getStringInput(
                            "Choose coupon id from the list",
                            getCompanyCouponsId(companyFacade));
                    companyFacade.deleteCoupon(Long.parseLong(couponToDelete));
                    System.out.println("Coupon with id: " + couponToDelete + " was deleted");
                    break;
                case "4":
                    switch (getStringInput("""
                                    Choose a filter
                                    1. No filters
                                    2. Category filter
                                    3. Max Price filter""",
                            Arrays.asList("1", "2", "3")
                    )) {
                        case "1" -> System.out.println(companyFacade.getCompanyCoupons().toString());
                        case "2" -> System.out.println(companyFacade.getCompanyCoupons(
                                getCategory("Please enter a category name")
                        ));
                        case "3" -> System.out.println(companyFacade.getCompanyCoupons(
                                getDoubleInput("Please enter max price")
                        ));
                    }
                    break;
                case "5":
                    quit = true;
                    break;
            }
        }
    }

    private static List<String> getCompanyCouponsId(CompanyFacade companyFacade) throws CrudException {
        List<String> availableCouponsId = new ArrayList<>();
        for (Coupon coupon : companyFacade.getCompanyCoupons()) {
            availableCouponsId.add(String.valueOf(coupon.getId()));
        }
        return availableCouponsId;
    }

    private static void openAdminMenu(AdminFacade adminFacade) throws InterruptedException, EmailExistException, CrudException, NameExistException, UpdateException {
        boolean quit = false;
        System.out.println("Welcome admin");
        while (!quit) {
            System.out.println("""
                    Press 1 to create a new company
                    press 2 to update a company's email
                    Press 3 to get a company details
                    Press 4 to get all companies details
                    press 5 to delete a company 
                    press 6 to create a new customer
                    press 7 to update a customer's first name
                    Press 8 to get a customer details
                    press 9 to get all customers details
                    press 10 to delete a customer
                    Press R to return to the main menu
                    """);
            //TODO: make a getInt with range
            String answer = getStringInput("Please choose a task from the list above", Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "R"));
            switch (answer) {
                case "1" -> {
                    Company company = createCompany();
                    final long id = adminFacade.addNewCompany(company);
                    System.out.println(company.getName() + " was added to the DB (id:" + id + ")");
                }
                case "2" -> {
                    String compToUpdateId = getStringInput("Select a company id to update", getAvailableCompId(adminFacade));
                    final Company compToUpdate = adminFacade.getOneCompany(Long.parseLong(compToUpdateId));
                    final String newEmail = getStringInput("Enter new company email");
                    compToUpdate.setEmail(newEmail);
                    adminFacade.updateCompany(compToUpdate);
                    System.out.println("Company email was updated");
                }
                case "3" -> {
                    String companyToGetId = getStringInput("Enter company from the available companies list", getAvailableCompId(adminFacade));
                    System.out.println(adminFacade.getOneCompany(Long.parseLong(companyToGetId)));
                }
                case "4" -> {
                    final ArrayList<Company> allExistsCompanies = adminFacade.getAllCompanies();
                    System.out.println("All companies details");
                    System.out.println(allExistsCompanies.toString());
                }
                case "5" -> {
                    String compToDeleteId = getStringInput("Select a company id to delete", getAvailableCompId(adminFacade));
                    adminFacade.deleteCompany(Long.parseLong(compToDeleteId));
                    System.out.println("Company with id " + compToDeleteId + " was deleted from the DB");
                }
                case "6" -> {
                    Customer newCustomer = createCustomer();
                    final long newCustomerId = adminFacade.addNewCustomer(newCustomer);
                    System.out.println(newCustomer.getFirstName() + " was added to the DB. (id:" + newCustomerId + ")");
                }
                case "7" -> {
                    String custToUpdateId = getStringInput("Please choose a customer id from the list", getAvailableCustId(adminFacade));
                    Customer custToUpdate = adminFacade.getOneCustomer(Long.parseLong(custToUpdateId));
                    String newFirstName = getStringInput("enter new customer frist name");
                    custToUpdate.setFirstName(newFirstName);
                    adminFacade.updateCustomer(custToUpdate);
                    System.out.println("Customer wht id:" + custToUpdateId + " first name is now - " + newFirstName);
                }
                case "8" -> {
                    String custToGetId = getStringInput("Please choose a customer id from the list", getAvailableCustId(adminFacade));
                    System.out.println(adminFacade.getOneCustomer(Long.parseLong(custToGetId)));
                }
                case "9" -> {
                    System.out.println("All customers details");
                    System.out.println(adminFacade.getAllCustomers().toString());
                }
                case "10" -> {
                    String custToDeleteId = getStringInput("Please choose a customer id from the list", getAvailableCustId(adminFacade));
                    adminFacade.deleteCustomer(Long.parseLong(custToDeleteId));
                }
                case "R" -> quit = true;
            }
        }

    }

    private static List<String> getAvailableCompId(AdminFacade adminFacade) throws CrudException, InterruptedException {
        //Print available companies id
        ArrayList<Company> allCompanies = adminFacade.getAllCompanies();
        List<String> availableCompsId = new ArrayList<>();
        for (Company comp :
                allCompanies) {
            availableCompsId.add(String.valueOf(comp.getId()));
        }
        return availableCompsId;
    }

    private static List<String> getAvailableCustId(AdminFacade adminFacade) throws CrudException {
        //Print available customers ids
        ArrayList<Customer> allCustomers = adminFacade.getAllCustomers();
        List<String> availableCustId = new ArrayList<>();
        for (Customer customer :
                allCustomers) {
            availableCustId.add(String.valueOf(customer.getId()));
        }
        return availableCustId;
    }

    private static void printMainMenu() {
        System.out.println("************************************\n" +
                           "MAIN MENU\n" +
                           "Daily Job status = " + getExpirationJobStatus(expirationDailyJob.isStop()) + "\n" +
                           "press 0 to turn it " + getExpirationJobStatus(!expirationDailyJob.isStop()) + "\n" +
                           "Press 1 to log in\n" +
                           "Press 2 to start all tests\n" +
                           "Press 3 to quit\n" +
                           "Type DOCS to open the docs on your web browser\n"+
                           "************************************");
    }

    private static String getExpirationJobStatus(boolean isStopped) {
        String expirationJobStatus;
        if (isStopped) expirationJobStatus = "off";
        else expirationJobStatus = "on";
        return expirationJobStatus;
    }

    /**
     * Company creation method from user input
     *
     * @return generated Company object (without id)
     */
    private static Company createCompany() {
        System.out.println("Create new company");
        return new Company(
                getStringInput("Please enter company name"),
                getStringInput("Please enter company email"),
                getStringInput("Please enter company password")
        );
    }

    /**
     * Customer creation method from user input
     *
     * @return generated Customer object (without id)
     */
    public static Customer createCustomer() {
        System.out.println("Create new customer");
        return new Customer(
                getStringInput("Please enter first name"),
                getStringInput("Please enter last name"),
                getStringInput("Please enter email"),
                getStringInput("Please enter customer password")
        );
    }

    /**
     * Coupon creation method from user input
     *
     * @return generated Coupon object (without id)
     */
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
                getIntInput("please enter coupon amount"),
                getDoubleInput("please enter coupon price"),
                getStringInput("Enter coupon image url")
        );
    }

}