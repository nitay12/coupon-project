package com.nitay.couponproject.facades;

import com.nitay.couponproject.dal.CompaniesDBDAO;
import com.nitay.couponproject.dal.CouponsDBDAO;
import com.nitay.couponproject.dal.CustomersDBDAO;
import com.nitay.couponproject.dal.interfaces.CompaniesDAO;
import com.nitay.couponproject.dal.interfaces.CouponsDAO;
import com.nitay.couponproject.dal.interfaces.CustomersDAO;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;

/**
 * Abstraction of the customer facade. Contains within it all the DAO (currently declared in the constructor)
 *
 * @see CouponsDAO
 * @see CompaniesDAO
 * @see CustomersDAO
 * @see AdminFacade
 * @see CompanyFacade
 * @see CustomerFacade
 */
//TODO Change all the facades to be dependency injected
//     (currently the DB DAOs declared in the ClientFacade constructor)
public abstract class ClientFacade {
    ClientFacade() {
        this.companiesDAO = CompaniesDBDAO.getInstance();
        this.customersDAO = CustomersDBDAO.getInstance();
        this.couponsDAO = CouponsDBDAO.getInstance();
    }

    protected CompaniesDAO companiesDAO;
    protected CustomersDAO customersDAO;
    protected CouponsDAO couponsDAO;

    /**
     * @param email Client's email
     * @param password Client's password
     * @return Boolean if log in succeeds
     * @throws WrongCredentialsException When email/password not exist or match
     * @throws CrudException If there is problem of getting all clients (used to compare email and password)
     */
    protected abstract boolean login(String email, String password) throws WrongCredentialsException, CrudException, InterruptedException;
}
