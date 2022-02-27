package com.nitay.couponproject.dal.interfaces;

import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.model.Company;

import java.util.ArrayList;

public interface CompaniesDAO {
    boolean loginCompany(String email, String password) throws CrudException;
    long addCompany(Company company) throws CrudException;
    void updateCompany(Company company) throws CrudException;
    void deleteCompany(int companyID) throws CrudException;
    ArrayList<Company> getAllCompanies() throws CrudException;
    Company getOneCompany(long companyID) throws CrudException;
}
