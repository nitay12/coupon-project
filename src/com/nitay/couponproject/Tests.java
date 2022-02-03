package com.nitay.couponproject;

import com.nitay.couponproject.dal.CompaniesDBDAO;
import com.nitay.couponproject.dal.CouponsDBDAO;
import com.nitay.couponproject.dal.CustomerDBDAO;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;

public class Tests {
    final static Scanner SCANNER = new Scanner(System.in);

    public static Company createCompany() {
        Company c = new Company();
        System.out.println("Please enter company name");
        c.setName(SCANNER.next());
        System.out.println("Please enter company email");
        c.setEmail(SCANNER.next());
        System.out.println("Please enter company password");
        c.setPassword(SCANNER.next());
        return c;
    }

    public static Customer createCustomer() {
        Customer c = new Customer();
        System.out.println("Please enter customer first name");
        c.setFirstName(SCANNER.next());
        System.out.println("Please enter customer last name");
        c.setLastName(SCANNER.next());
        System.out.println("Please enter customer email");
        c.setEmail(SCANNER.next());
        System.out.println("Please enter customer password");
        c.setPassword(SCANNER.next());
        return c;
    }

    public static Coupon createCoupon() {
        Coupon c = new Coupon();
        System.out.println("Please enter company id");
        c.setCompanyID(SCANNER.nextInt());
        System.out.println("Please enter coupon category id");
        c.setCategoryID(SCANNER.nextInt());
        System.out.println("Please enter coupon title");
        c.setTitle(SCANNER.next());
        System.out.println("Please enter coupon description");
        c.setDescription(SCANNER.next());
        System.out.println("Setting start date to this day date");
        c.setStartDate(Date.valueOf(LocalDate.now()));
        c.setEndDate(Date.valueOf(LocalDate.now()));
        System.out.println("Please enter coupon amount");
        c.setCategoryID(SCANNER.nextInt());
        System.out.println("Please enter coupon price");
        c.setPrice(SCANNER.nextDouble());
        System.out.println("Please enter coupon image url");
        c.setImage(SCANNER.next());
        return c;
    }

    public static int getId() {
        System.out.println("please enter id: ");
        return SCANNER.nextInt();
    }

    public static void main(String[] args) {
//    //COMPANIES DAO TESTS:
//        System.out.println("getAllCompanies:"+CompaniesDBDAO.instance.getAllCompanies().toString());
//        System.out.println("getOneCompany:"+CompaniesDBDAO.instance.getOneCompany(1).toString());
//        System.out.println("addCompany:"+CompaniesDBDAO.instance.addCompany(createCompany()));
//        System.out.println("isCompanyExist: "+CompaniesDBDAO.instance.isCompanyExist("castro@gmail.com","123456"));
//        System.out.println("deleteCompany:"); CompaniesDBDAO.instance.deleteCompany(getId());
//
//    //CUSTOMERS DAO TESTS:
//        System.out.println("getAllCostumers:"+ CustomerDBDAO.instance.getAllCustomers().toString());
//        System.out.println("addCustomer: added customer with id:"+CustomerDBDAO.instance.addCustomer(createCustomer()));
//        System.out.println("getOneCustomers: id:1"+CustomerDBDAO.instance.getOneCustomer(getId()).toString());
//        System.out.println("isCustomerExist: "+CustomerDBDAO.instance.isCustomerExist("mc@gmail.com","123456"));
//        System.out.println("deleteCustomer:"); CustomerDBDAO.instance.deleteCustomer(getId());

    //CUSTOMERS DAO TESTS:
        System.out.println("addCoupon: added coupon with id:"+CouponsDBDAO.instance.addCoupon(createCoupon()));
        System.out.println("getAllCoupons:"+ CouponsDBDAO.instance.getAllCoupons().toString());
        System.out.println("getOneCoupon: "+CouponsDBDAO.instance.getOneCoupon(getId()).toString());
        System.out.println("deleteCoupon:"); CouponsDBDAO.instance.deleteCoupon(getId());
        //

    }
}
