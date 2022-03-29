package com.nitay.couponproject.tests;

import com.nitay.couponproject.exceptions.*;
import com.nitay.couponproject.utils.DBInitializer;

import java.util.Arrays;

import static com.nitay.couponproject.tests.AdminTests.allAdminTests;
import static com.nitay.couponproject.tests.CompanyTests.allCompanyTests;
import static com.nitay.couponproject.tests.CustomerTests.allCustomerTests;
import static com.nitay.couponproject.utils.MyScanner.getStringInput;

/**
 * Runs all the app methods one by one
 */
public class Tests {
    /**
     * Runs all the test methods
     */
    public static void testAll() throws WrongCredentialsException, InterruptedException, CrudException, NameExistException, EmailExistException, UpdateException {
            allAdminTests();
            allCompanyTests();
            allCustomerTests();
    }

    public static void main(String[] args) {
        try {
            switch (
                    getStringInput(
                            "Would you like to reinitialize the project database with mock data?)",
                            Arrays.asList("y", "n"))) {
                case "y":
                    DBInitializer.dropSchema();
                    DBInitializer.setupDatabase();
                    DBInitializer.insertMockData();
                    break;
                case "n":
                    break;
            }
            testAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}