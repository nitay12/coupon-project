package com.nitay.couponproject.dal.interfaces;

import com.nitay.couponproject.model.Company;

import java.util.ArrayList;

public interface CompaniesDAO {
    boolean isCompanyExist(String email, String password);
    long addCompany(Company company);
    void updateCompany(Company company);
    void deleteCompany(int companyID);
    ArrayList<Company> getAllCompanies();
    Company getOneCompany(int companyID);
}
