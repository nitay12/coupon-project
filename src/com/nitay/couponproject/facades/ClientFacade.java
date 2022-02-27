package com.nitay.couponproject.facades;

import com.nitay.couponproject.dal.CompaniesDBDAO;
import com.nitay.couponproject.dal.CouponsDBDAO;
import com.nitay.couponproject.dal.CustomerDBDAO;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;

public abstract class ClientFacade {
    ClientFacade(){
        this.companiesDBDAO = CompaniesDBDAO.instance;
        this.customerDBDAO = CustomerDBDAO.instance;
        this.couponsDBDAO = CouponsDBDAO.instance;
    }
    protected CompaniesDBDAO companiesDBDAO;
    protected CustomerDBDAO customerDBDAO;
    protected CouponsDBDAO couponsDBDAO;
    protected abstract boolean login(String email, String password) throws WrongCredentialsException, CrudException;
}
