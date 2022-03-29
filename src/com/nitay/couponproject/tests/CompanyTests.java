package com.nitay.couponproject.tests;

import com.nitay.couponproject.enums.ClientType;
import com.nitay.couponproject.exceptions.CouponTitleExistException;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.UpdateException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.facades.CompanyFacade;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.utils.LoginManager;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import static com.nitay.couponproject.utils.MyScanner.*;

public class CompanyTests {
    public static void allCompanyTests() {
        try {
            System.out.println("COMPANY TESTS");
            //Log in test
            System.out.println("LOGIN COMPANY");
            System.out.println("email:company1@email.com, password:123456");
            CompanyFacade companyFacade = loginTest();
            System.out.println("LOG IN SUCCEED");
            //Get company details test
            System.out.println("LOGGED IN COMPANY DETAILS:");
            System.out.println(companyFacade.loggedInCompanyDetails().toString());
            //Create coupon object
            Coupon coupon = new Coupon(
                    Category.Electricity,
                    "TestCoupon",
                    "A coupon for testing purpose",
                    Date.valueOf(LocalDate.now()),
                    Date.valueOf(LocalDate.of(2023,2,2)),
                    20,
                    30.5,
                    "https://company/images/image1.jpg"
            );
            long newCouponId = addCouponTest(companyFacade, coupon);
            //Update coupon test
            updateCouponTest(companyFacade, coupon, "updated coupon");
            //Get company's coupons tests
            System.out.println("GET COMPANY'S COUPONS TESTS");
            getCompanyCouponsTest(companyFacade);
            //Delete coupon
            deleteCouponTest(companyFacade, newCouponId);
        } catch (CrudException | WrongCredentialsException | CouponTitleExistException | UpdateException | NullPointerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void deleteCouponTest(CompanyFacade companyFacade, long couponId) throws CrudException {
        final String answer = getStringInput("Would you like to delete the coupon?", Arrays.asList("y", "n"));
        switch (answer) {
            case "y":
                companyFacade.deleteCoupon((int) couponId);
                System.out.println("coupon with id: " + couponId + " deleted");
                break;
            case "n":
                break;
        }
    }

    private static void getCompanyCouponsTest(CompanyFacade companyFacade) throws CrudException, InterruptedException {
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
    }

    private static void updateCouponTest(CompanyFacade companyFacade, Coupon coupon, String newTitle) throws UpdateException, CrudException {
        System.out.println("Update coupon");
        coupon.setTitle(newTitle);
        companyFacade.updateCoupon(coupon);
        System.out.println("coupon with id: " + coupon.getId() + " updated");
    }

    private static long addCouponTest(CompanyFacade companyFacade, Coupon newCoupon) throws CouponTitleExistException, CrudException {
        //Add coupon test
        System.out.println("Add coupon test");
        long newCouponId = companyFacade.addCoupon(newCoupon);
        newCoupon.setId((int) newCouponId);
        System.out.println("coupon with id: " + newCouponId + " added");
        return newCouponId;
    }

    private static CompanyFacade loginTest() throws WrongCredentialsException, CrudException, InterruptedException {
        System.out.println("LOGIN COMPANY TEST");
//        System.out.println("Wrong credentials exception test");
//        LoginManager.getInstance().login("wrong", "credentials", ClientType.Company)
        return (CompanyFacade) LoginManager.getInstance().login(
                "company1@email.com",
                "123456",
                ClientType.Company
        );
    }
}
