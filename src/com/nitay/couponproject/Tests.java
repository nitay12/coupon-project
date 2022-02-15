package com.nitay.couponproject;

import com.nitay.couponproject.facades.AdminFacade;
import com.nitay.couponproject.model.Company;

import static com.nitay.couponproject.utils.MyScanner.getIntInput;
import static com.nitay.couponproject.utils.MyScanner.getStringInput;

public class Tests {
    public static Company createCompany(){
        return new Company(
                getStringInput("Please enter company name: "),
                getStringInput("Please enter company email: "),
                getStringInput("Please enter company password: ")
        );
    }
    public static void main(String[] args) {
//      Admin facade
//        System.out.println("Admin facade add company: "+ AdminFacade.instance.addNewCompany(createCompany()));
        Company company = createCompany();
        company.setId(getIntInput("enter company id"));
        AdminFacade.instance.updateCompany(company);
    }
}
