package com.nitay.couponproject.facades;

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
            ArrayList<Company> allCompanies = companiesDBDAO.getAllCompanies();
            isCompanyNameExist(company, allCompanies);
            isCompanyEmailExist(company, allCompanies);
            return companiesDBDAO.addCompany(company);
        } catch (NameExistException | EmailExistException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateCompany(Company company) {
        try {
            Company companyToUpdate = companiesDBDAO.getOneCompany(company.getId());
            if (!company.getName().equals(companyToUpdate.getName())) {
                throw new UpdateException("Name cannot be updated");
            } else {
                companiesDBDAO.updateCompany(company);
            }
        } catch (UpdateException e) {
            e.printStackTrace();
        }
    }

    public void deleteCompany(int companyId) {
        try {
            companiesDBDAO.deleteCompany(companyId);
            couponsDBDAO.deleteCompanyCoupons(companyId);
            ArrayList<Coupon> coupons = couponsDBDAO.getAllCoupons();
            for (Coupon coupon :
                    coupons) {
                if (coupon.getCompanyID() == companyId) {
                    couponsDBDAO.deleteCouponPurchase(coupon.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Company> getAllCompanies() {
        ArrayList<Company> allCompanies = companiesDBDAO.getAllCompanies();
        for (Company company :
                allCompanies) {
            company.setCoupons(couponsDBDAO.getCompanyCoupons(company.getId()));
        }
        return allCompanies;
    }

    public Company getOneCompany(int companyId) {
        Company company = companiesDBDAO.getOneCompany(companyId);
        company.setCoupons(couponsDBDAO.getCompanyCoupons(companyId));
        return company;
    }

    public long addNewCustomer(Customer customer) {
        try {
            ArrayList<Customer> allCustomers = customerDBDAO.getAllCustomers();
            isCustomerEmailExist(customer, allCustomers);
            return customerDBDAO.addCustomer(customer);
        } catch (EmailExistException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void updateCustomer(Customer customer) {
        customerDBDAO.updateCustomer(customer);
    }

    public void deleteCustomer(int customerId) {
        customerDBDAO.deleteCustomer(customerId);
        couponsDBDAO.deleteCustomerPurchase(customerId);
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> allCustomers = customerDBDAO.getAllCustomers();
        for (Customer customer :
                allCustomers) {
            customer.setCoupons(couponsDBDAO.getCustomerCoupons(customer.getId()));
        }
        return allCustomers;
    }

    public Customer getOneCustomer(int customerId) {
        Customer customer = customerDBDAO.getOneCustomer(customerId);
        customer.setCoupons(couponsDBDAO.getCustomerCoupons(customer.getId()));
        return customer;
    }


}