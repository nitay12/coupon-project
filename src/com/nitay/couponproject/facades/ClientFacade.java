package com.nitay.couponproject.facades;

import com.nitay.couponproject.dal.CompaniesDBDAO;
import com.nitay.couponproject.dal.CouponsDBDAO;
import com.nitay.couponproject.dal.CustomersDBDAO;
import com.nitay.couponproject.dal.interfaces.CompaniesDAO;
import com.nitay.couponproject.dal.interfaces.CouponsDAO;
import com.nitay.couponproject.dal.interfaces.CustomersDAO;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;

public abstract class ClientFacade {
    ClientFacade(){
        this.companiesDAO = CompaniesDBDAO.getInstance();
        this.customersDAO = CustomersDBDAO.getInstance();
        this.couponsDAO = CouponsDBDAO.getInstance();
    }
    protected CompaniesDAO companiesDAO;
    protected CustomersDAO customersDAO;
    protected CouponsDAO couponsDAO;
    protected abstract boolean login(String email, String password) throws WrongCredentialsException, CrudException;
}
