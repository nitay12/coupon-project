package com.nitay.couponproject.tests;

import com.nitay.couponproject.dal.CouponsDBDAO;
import com.nitay.couponproject.enums.ClientType;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.facades.CustomerFacade;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.utils.LoginManager;

import java.util.ArrayList;

import static com.nitay.couponproject.utils.MyScanner.*;
import static com.nitay.couponproject.utils.MyScanner.getDoubleInput;

public class CustomerTests {
    public static void allCustomerTests() throws WrongCredentialsException, CrudException, InterruptedException {
        System.out.println("CUSTOMER TESTS");
        //Login test
        CustomerFacade customerFacade = loginTest();
        //Customer details test
        System.out.println("Logged in customer details: ");
        System.out.println(customerFacade.getCustomerDetails().toString());
        //Purchase Coupon test
        purchaseCouponTest(customerFacade);
        //Get customer coupons tests
        getCustomerCouponsTests(customerFacade);
    }

    private static void getCustomerCouponsTests(CustomerFacade customerFacade) throws CrudException {
        //Get customer coupons methods
        //Get all customer coupons
        System.out.println("All customer coupons");
        System.out.println(customerFacade.getCustomerCoupons());
        //Get customer coupons by category
        System.out.println("Customer coupons by category");
        Category category = getCategory("Please enter category name");
        System.out.println(customerFacade.getCustomerCoupons(category));
        //Get customer coupons by category
        System.out.println("Customer coupons by max price");
        double maxPrice = getDoubleInput("Please enter max price");
        System.out.println(customerFacade.getCustomerCoupons(maxPrice));
    }

    private static void purchaseCouponTest(CustomerFacade customerFacade) throws CrudException {
        ArrayList<Coupon> availableCoupons = CouponsDBDAO.getInstance().getAllCoupons();
        System.out.println("all coupons in the DB: ");
        System.out.println(availableCoupons.toString());
        Integer chosenCouponId = getIntInput("Please choose coupon id to purchase");
        Coupon chosenCoupon = CouponsDBDAO.getInstance().getOneCoupon(chosenCouponId);
        customerFacade.purchaseCoupon(chosenCoupon);
    }

    private static CustomerFacade loginTest() throws WrongCredentialsException, CrudException, InterruptedException {
        System.out.println("CUSTOMER LOGIN TEST");
        System.out.println("credentials - email:Customer1@email.com, password:123456");
        CustomerFacade customerFacade = (CustomerFacade) LoginManager
                .getInstance()
                .login(
                        "Customer1@test.com",
                        "123456",
                        ClientType.Customer
                );
        return customerFacade;
    }
}
