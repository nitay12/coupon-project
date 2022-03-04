package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.EmailExistException;
import com.nitay.couponproject.exceptions.NameExistException;
import com.nitay.couponproject.exceptions.UpdateException;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.model.Customer;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class AdminFacade extends ClientFacade {
    public static AdminFacade instance = new AdminFacade();

    @Override
    public boolean login(String email, String password) {
        //TODO:Real login implementation
//        Hard coded implementation as mentioned in the instructions:
        String ADMIN_MAIL = "admin@admin.com";
        String ADMIN_PASS = "admin";
        return email.equals(ADMIN_MAIL) && password.equals(ADMIN_PASS);
    }

    private void isCompanyNameExist(Company company, ArrayList<Company> companies) throws NameExistException {
        for (Company c :
                companies) {
            if (company.getName().equals(c.getName())) {
                throw new NameExistException("Sorry this name already exist");
            }
        }
    }

    //TODO: generify the isEmailExist method
    private void isCompanyEmailExist(Company company, ArrayList<Company> companies) throws EmailExistException {
        for (Company c :
                companies) {
            if (company.getEmail().equals(c.getEmail())) {
                throw new EmailExistException("Sorry this email already exist");
            }
        }
    }

    private void isCustomerEmailExist(Customer customer, ArrayList<Customer> allCustomers) throws EmailExistException {
        for (Customer c : allCustomers) {
            if (customer.getEmail().equalsIgnoreCase(c.getEmail())) {
                throw new EmailExistException("This email already exist");
            }
        }
    }

    public long addNewCompany(Company company) {
        try {
            ArrayList<Company> allCompanies = companiesDAO.getAllCompanies();
            isCompanyNameExist(company, allCompanies);
            isCompanyEmailExist(company, allCompanies);
            return companiesDAO.addCompany(company);
        } catch (NameExistException | EmailExistException | CrudException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateCompany(Company company) {
        try {
            Company companyToUpdate = companiesDAO.getOneCompany(company.getId());
            if (!company.getName().equals(companyToUpdate.getName())) {
                throw new UpdateException("Name cannot be updated");
            } else {
                companiesDAO.updateCompany(company);
            }
        } catch (UpdateException | CrudException e) {
            e.printStackTrace();
        }
    }

    public void deleteCompany(int companyId) {
        try {
            companiesDAO.deleteCompany(companyId);
            couponsDAO.deleteCompanyCoupons(companyId);
            ArrayList<Coupon> coupons = couponsDAO.getAllCoupons();
            for (Coupon coupon :
                    coupons) {
                if (coupon.getCompanyID() == companyId) {
                    couponsDAO.deletePurchaseByCouponId(coupon.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Company> getAllCompanies() throws CrudException {
        ArrayList<Company> allCompanies = companiesDAO.getAllCompanies();
        for (Company company :
                allCompanies) {
            company.setCoupons(couponsDAO.getCompanyCoupons(company.getId()));
        }
        return allCompanies;
    }

    public Company getOneCompany(int companyId) throws CrudException {
        Company company = companiesDAO.getOneCompany(companyId);
        company.setCoupons(couponsDAO.getCompanyCoupons(companyId));
        return company;
    }

    public long addNewCustomer(Customer customer) {
        try {
            ArrayList<Customer> allCustomers = customersDAO.getAllCustomers();
            isCustomerEmailExist(customer, allCustomers);
            return customersDAO.addCustomer(customer);
        } catch (EmailExistException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void updateCustomer(Customer customer) {
        customersDAO.updateCustomer(customer);
    }

    public void deleteCustomer(int customerId) throws CrudException {
        couponsDAO.deletePurchaseByCustomerId(customerId);
        customersDAO.deleteCustomer(customerId);
        couponsDAO.deleteCustomerPurchase(customerId);
    }

    public ArrayList<Customer> getAllCustomers() throws CrudException {
        ArrayList<Customer> allCustomers = customersDAO.getAllCustomers();
        for (Customer customer :
                allCustomers) {
            customer.setCoupons(couponsDAO.getCustomerCoupons(customer.getId()));
        }
        return allCustomers;
    }

    public Customer getOneCustomer(int customerId) throws CrudException {
        Customer customer = customersDAO.getOneCustomer(customerId);
        customer.setCoupons(couponsDAO.getCustomerCoupons(customer.getId()));
        return customer;
    }


}