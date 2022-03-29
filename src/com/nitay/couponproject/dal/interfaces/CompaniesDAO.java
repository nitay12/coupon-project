package com.nitay.couponproject.dal.interfaces;

import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.model.Company;

import java.util.ArrayList;

/**
 * An interface for companies data access object
 */
public interface CompaniesDAO {
    /**
     * @deprecated
     * @param email Company email
     * @param password Company password
     * @return Login status (boolean)
     * @throws CrudException if something gets wrong
     */
    //---I deleted the loginCompany method because its a logic method it was implemented in CompanyFacade---
    boolean loginCompany(String email, String password) throws CrudException, InterruptedException;

    // TODO: Generify a Crud DAO and implement on all DAOs

    //Crud operations:

    /**
     * Adds a new company to the database
     *
     * @param company A Company object (no Id required)
     * @return Auto generated company ID
     * @throws CrudException if something gets wrong.
     * @see Company
     * @see CrudException
     */
    long addCompany(Company company) throws CrudException, InterruptedException;

    /**
     * Updates an existing company in the database
     *
     * @param company A full Company object (Id param is required)
     * @throws CrudException if something gets wrong.
     * @see Company
     * @see CrudException
     */

    void updateCompany(Company company) throws CrudException, InterruptedException;

    /**
     * Deletes a company from the database
     *
     * @param companyID The company id in the database
     * @throws CrudException if something gets wrong.
     * @see CrudException
     */
    void deleteCompany(long companyID) throws CrudException, InterruptedException;

    /**
     * Gets all companies that exist in the database
     *
     * @return An ArrayList of all companies
     * @throws CrudException if something gets wrong.
     * @see Company
     * @see CrudException
     */
    ArrayList<Company> getAllCompanies() throws CrudException, InterruptedException;

    /**
     * Gets a company from the database by given ID
     *
     * @param companyID The company id in the database
     * @return A Company object
     * @throws CrudException if something gets wrong.
     * @see Company
     * @see CrudException
     */
    Company getOneCompany(long companyID) throws CrudException, InterruptedException;
}
