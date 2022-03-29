package com.nitay.couponproject.dal;

import com.nitay.couponproject.dal.interfaces.CompaniesDAO;
import com.nitay.couponproject.enums.CrudType;
import com.nitay.couponproject.enums.EntityType;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.facades.ClientFacade;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.utils.ConnectionPool;
import com.nitay.couponproject.utils.ObjectExtractionUtil;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;

/**
 * A singleton data access object, Implements CompaniesDAO interface to make CRUD operations on a SQL database.
 * CompaniesDBDAO is connected to the ConnectionPool and used in the facades.
 *
 * @see CompaniesDAO
 * @see ConnectionPool
 * @see ClientFacade
 */
public class CompaniesDBDAO implements CompaniesDAO {
    @Getter
    private static final CompaniesDBDAO instance = new CompaniesDBDAO();
    private ConnectionPool connectionPool;

    /**
     * Sets the connectionPool variable to the {@link ConnectionPool} instance
     */
    private CompaniesDBDAO() {
        try {
            this.connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---I deleted the loginCompany method because its a logic method it was implemented in CompanyFacade---

    /**
     * @param email    Company email
     * @param password Company password
     * @return Login status (boolean)
     * @throws CrudException if something gets wrong
     * @deprecated For being a logic method it was implemented in CompanyFacade
     */
    @Override
    public boolean loginCompany(final String email, final String password) throws CrudException, InterruptedException {
        ArrayList<Company> companies = getAllCompanies();
        for (Company c :
                companies) {
            if (c.getEmail().equals(email) && c.getPassword().equals(String.valueOf(password.hashCode()))) {
                return true;
            }
        }
        return false;
    }

    //

    /**
     * Adds a new company to the database
     *
     * @param company A Company object (no Id required)
     * @return Auto generated company ID
     * @throws CrudException - if something gets wrong.
     * @see Company
     * @see CrudException
     */
    @Override
    public long addCompany(final Company company) throws CrudException, InterruptedException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "INSERT INTO coupons_project.companies (name, email, password) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setLong(3, company.getPassword().hashCode());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new RuntimeException("Failed to retrieve company id");
            }
            connectionPool.returnConnection(connection);
            return generatedKeysResult.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COMPANY, CrudType.CREATE);
        }
    }

    /**
     * Updates an existing company in the database
     *
     * @param company A full Company object (Id param is required)
     * @throws CrudException - if something gets wrong.
     * @see Company
     * @see CrudException
     */

    @Override
    public void updateCompany(Company company) throws CrudException, InterruptedException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "UPDATE coupons_project.companies SET name = ?,email = ?, password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, String.valueOf(company.getPassword().hashCode()));
            preparedStatement.setLong(4, company.getId());
            preparedStatement.executeUpdate();
            connectionPool.returnConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COMPANY, CrudType.UPDATE);
        }
    }

    /**
     * Deletes a company from the database
     *
     * @param companyID The company id in the database
     * @throws CrudException if something gets wrong.
     * @see CrudException
     */
    @Override
    public void deleteCompany(long companyID) throws CrudException, InterruptedException {
        String sqlStatement = "DELETE FROM coupons_project.companies WHERE (id = ?)";
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, companyID);
            preparedStatement.executeUpdate();
            connectionPool.returnConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COMPANY, CrudType.DELETE);
        }
    }

    /**
     * Gets all companies that exist in the database
     *
     * @return An ArrayList of all companies
     * @throws CrudException if something gets wrong.
     * @see Company
     * @see CrudException
     */
    @Override
    public ArrayList<Company> getAllCompanies() throws CrudException, InterruptedException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.companies";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Company> companies = new ArrayList();
            while (result.next()) {
                companies.add(ObjectExtractionUtil.resultToCompany(result));
            }
            connectionPool.returnConnection(connection);
            return companies;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COMPANY, CrudType.READ_ALL);
        }
    }

    /**
     * Gets a company from the database by given ID
     *
     * @param companyID The company id in the database
     * @return A Company object
     * @throws CrudException if something gets wrong.
     * @see Company
     * @see CrudException
     */

    @Override
    public Company getOneCompany(long companyID) throws CrudException, InterruptedException {
        try {
            Connection connection = connectionPool.getConnection();
            String sqlStatement = "SELECT * FROM coupons_project.companies WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, companyID);
            ResultSet result = preparedStatement.executeQuery();
            if (!result.next()) {
                return null;
            }
            Company company = ObjectExtractionUtil.resultToCompany(result);
            ConnectionPool.getInstance().returnConnection(connection);
            return company;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CrudException(EntityType.COMPANY, CrudType.READ);
        }
    }
}
