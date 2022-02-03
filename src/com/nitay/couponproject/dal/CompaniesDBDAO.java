package com.nitay.couponproject.dal;

import com.nitay.couponproject.dal.interfaces.CompaniesDAO;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.utils.JDBCUtil;
import com.nitay.couponproject.utils.ObjectExtractionUtil;

import java.sql.*;
import java.util.ArrayList;

public class CompaniesDBDAO implements CompaniesDAO {
    public static final CompaniesDBDAO instance = new CompaniesDBDAO();

    private CompaniesDBDAO() {
        try {
            connection = JDBCUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection with database was failed");
        }
    }

    private final Connection connection;

    public boolean isCompanyExist(final String email, final String password) {
        ArrayList<Company> companies = getAllCompanies();
        for (Company c :
                companies) {
            if(c.getEmail().equals(email)&&c.getPassword().equals(String.valueOf(password.hashCode()))){
                return true;
            }
        }
            return false;
    }

    public long addCompany(final Company company) {
        try {
            String sqlStatement = "INSERT INTO companies (name, email, password) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setLong(3, company.getPassword().hashCode());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new RuntimeException("Failed to retrieve company id");
            }
            return generatedKeysResult.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a new company");
        }
    }

    public void updateCompany(Company company) {
        try {
            String sqlStatement = "UPDATE companies SET name = ?,email = ?, password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, String.valueOf(company.getPassword().hashCode()));
            preparedStatement.setLong(4, company.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update company");
        }
    }

    public void deleteCompany(int companyID) {
        String sqlStatement = "DELETE FROM companies WHERE (id = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, companyID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("deletion of company with id: " + companyID + " failed");
        }
    }

    public ArrayList<Company> getAllCompanies() {
        try {
            String sqlStatement = "SELECT * FROM companies";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<Company> companies = new ArrayList();
            while (result.next()) {
                companies.add(ObjectExtractionUtil.resultToCompany(result));
            }
            return companies;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all companies");
        }
    }

    @Override
    public Company getOneCompany(int companyID) {
        try {
            String sqlStatement = "SELECT * FROM companies WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, companyID);
            ResultSet result = preparedStatement.executeQuery();
            if (!result.next()) {
                return null;
            }
            return ObjectExtractionUtil.resultToCompany(result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve company with id: " + companyID);
        }
    }
}
