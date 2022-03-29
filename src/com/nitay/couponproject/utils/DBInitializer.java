package com.nitay.couponproject.utils;

import com.nitay.couponproject.dal.CompaniesDBDAO;
import com.nitay.couponproject.dal.CouponsDBDAO;
import com.nitay.couponproject.dal.CustomersDBDAO;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;
import com.nitay.couponproject.tasks.CouponExpirationDailyJob;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 * A database initialization utility class for testing.
 * <p>
 * Its methods are used to reinitialize the MySQL schema with its tables and configurations and add mock data to the database.
 */
public class DBInitializer {
    /**
     * Dropping the coupons_project schema if exists
     *
     * @throws SQLException If there is any SQL exception
     */
    public static void dropSchema() throws SQLException, InterruptedException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        System.out.println("Dropping coupons_project schema");
        connection.prepareStatement("DROP DATABASE IF EXISTS `coupons_project`;").execute();
        ConnectionPool.getInstance().returnConnection(connection);
    }

    /**
     * Reinitialize the coupons_project schema and it's tables.
     * <p>
     * Generated tables:
     * <ul>
     *     <li>Categories - initialized with the {@link Category} enum values</li>
     *     <li>Customers - no data</li>
     *     <li>Companies - no data</li>
     *     <li>Coupons - no data</li>
     *     <li>Customer_Coupon (coupons purchases) - no data</li>
     * </ul>
     *
     * @throws SQLException If there is any SQL exception
     **/
    public static void setupDatabase() throws SQLException, InterruptedException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        System.out.println("Creating coupons_project schema");
        connection.prepareStatement("CREATE DATABASE `coupons_project`").execute();

        System.out.println("Creating categories table");
        connection.prepareStatement(
                """
                        CREATE TABLE `coupons_project`.`categories` (
                          `name` varchar(45) NOT NULL,
                          PRIMARY KEY (`name`),
                          UNIQUE KEY `name_UNIQUE` (`name`)
                        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                        """).execute();

        System.out.println("Inserting Categories (enum) values to categories table");
        for (Category category : Category.values()) {
            PreparedStatement prep = connection.prepareStatement(
                    """
                            INSERT INTO `coupons_project`.`categories`
                            (`name`)
                            VALUES
                            (?);
                            """);
            prep.setString(1, category.name());
            prep.executeUpdate();
            System.out.println("Category " + category.name() + " was added to the database");
        }

        System.out.println("Creating companies table");
        connection.prepareStatement("""
                CREATE TABLE `coupons_project`.`companies` (
                  `id` bigint NOT NULL AUTO_INCREMENT,
                  `name` varchar(45) NOT NULL,
                  `email` varchar(45) NOT NULL,
                  `password` bigint NOT NULL,
                  PRIMARY KEY (`id`),
                  UNIQUE KEY `id_UNIQUE` (`id`),
                  UNIQUE KEY `name_UNIQUE` (`name`),
                  UNIQUE KEY `email_UNIQUE` (`email`)
                );""").execute();

        System.out.println("Creating customers table");
        connection.prepareStatement("""
                CREATE TABLE `coupons_project`.`customers` (
                  `id` bigint NOT NULL AUTO_INCREMENT,
                  `first_name` varchar(45) NOT NULL,
                  `last_name` varchar(45) NOT NULL,
                  `email` varchar(45) NOT NULL,
                  `password` bigint NOT NULL,
                  PRIMARY KEY (`id`),
                  UNIQUE KEY `id_UNIQUE` (`id`),
                  UNIQUE KEY `email_UNIQUE` (`email`)
                );
                """).execute();

        System.out.println("Creating coupons table");
        connection.prepareStatement("""
                CREATE TABLE `coupons_project`.`coupons` (
                  `id` bigint NOT NULL AUTO_INCREMENT,
                  `company_id` bigint NOT NULL,
                  `category` varchar(45) NOT NULL,
                  `title` varchar(45) NOT NULL,
                  `description` varchar(200) DEFAULT NULL,
                  `start_date` date NOT NULL,
                  `end_date` date NOT NULL,
                  `amount` int NOT NULL,
                  `price` double NOT NULL,
                  `image` varchar(200) DEFAULT NULL,
                  PRIMARY KEY (`id`),
                  UNIQUE KEY `id_UNIQUE` (`id`),
                  KEY `category_idx` (`category`),
                  KEY `company_id_idx` (`company_id`),
                  CONSTRAINT `category` FOREIGN KEY (`category`) REFERENCES `categories` (`name`),
                  CONSTRAINT `company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`)
                );
                """).execute();

        System.out.println("Creating customer_coupons table");
        connection.prepareStatement("""
                CREATE TABLE `coupons_project`.`customer_coupon` (
                  `customer_id` bigint NOT NULL,
                  `coupon_id` bigint NOT NULL,
                  PRIMARY KEY (`customer_id`,`coupon_id`),
                  KEY `coupon_id_idx` (`coupon_id`),
                  CONSTRAINT `coupon_id` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`),
                  CONSTRAINT `customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
                );
                """).execute();
        ConnectionPool.getInstance().returnConnection(connection);
    }

    /**
     * Inserting data to the database for testing.
     * <p>
     * Inserted data:
     * * <ul>
     * *     <li>Categories - {@link Category} values</li>
     * *     <li>Customers - filled with constant number of customers. ({@code int NUM_OF_CUSTOMERS})</li>
     * *     <li>Companies - filled with constant number of companies. ({@code int NUM_OF_COMPANIES})</li>
     * *     <li>Companies - filled with constant number of companies. ({@code int NUM_OF_COMPANIES})</li>
     * *     <li>Coupons - filled with random number of coupons per company. (up to {@code int MAX_COMPANY_COUPONS}). Every coupon gets random expiration month to test the {@link CouponExpirationDailyJob}, and random price to test getting coupons by price</li>
     * *     <li>Customer_Coupon (coupons purchases) - every customer purchase a coupon from the available coupons</li>
     * * </ul>
     *
     * @throws CrudException if there is any SQL exception
     */
    public static void insertMockData() throws CrudException, InterruptedException {
        //CONSTANTS
        final int NUM_OF_CUSTOMERS = 10;
        final int NUM_OF_COMPANIES = 10;
        final int MAX_COMPANY_COUPONS = 5;

        System.out.println("Inserting mock companies");
        for (int i = 0; i < NUM_OF_COMPANIES; i++) {
            final Company company = new Company(
                    "company" + (i + 1),
                    "company" + (i + 1) + "@email.com",
                    "123456"
            );
            company.setId(CompaniesDBDAO.getInstance().addCompany(company));
            System.out.println(company.getName() + " was added to the database");
            final int numOfCoupons = new Random().nextInt(MAX_COMPANY_COUPONS);
            System.out.println(company.getName() + " adding " + numOfCoupons + " coupons");
            for (int j = 0; j < numOfCoupons; j++) {
                final int maxPrice = 300;
                Coupon coupon = new Coupon(
                        Category.values()[new Random().nextInt(Category.values().length)],
                        "Title",
                        "Description",
                        Date.valueOf(LocalDate.of(2021, 1, 1)),
                        Date.valueOf(LocalDate.of(2022, 1 + new Random().nextInt(12), 1)),
                        100,
                        new Random().nextDouble() * maxPrice,
                        "http://" + company.getName() + "/image.jpg"
                );
                coupon.setCompanyID(company.getId());
                CouponsDBDAO.getInstance().addCoupon(coupon);
            }
        }
        System.out.println("Inserting customers");
        for (int i = 0; i < NUM_OF_CUSTOMERS; i++) {
            final Customer customer = new Customer(
                    "Customer" + (i + 1),
                    "Customer" + (i + 1),
                    "Customer" + (i + 1) + "@test.com",
                    "123456"
            );
            customer.setId(CustomersDBDAO.getInstance().addCustomer(customer));
            final ArrayList<Coupon> allCoupons = CouponsDBDAO.getInstance().getAllCoupons();
            System.out.println(customer.getFirstName() + " " + customer.getLastName() + " is purchasing coupon");
            CouponsDBDAO.getInstance().addCouponPurchase(customer.getId(), allCoupons.get(i).getId());
        }
    }
}
