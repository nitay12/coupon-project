package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.CouponTitleExistException;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.UpdateException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import com.nitay.couponproject.utils.LoginManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

//TODO: Verify that methods using companyId arent get null or 0 (throw not logged in exception)
@AllArgsConstructor
@NoArgsConstructor
public class CompanyFacade extends ClientFacade {
    /**
     * The logged in company id
     */
    private long companyId;

    /**
     * @param email    Company's email
     * @param password Company's password
     * @return True if login succeed, False if not
     * @throws WrongCredentialsException If email and password not match
     * @throws CrudException             If there is any SQL exception
     * @see LoginManager
     */
    //TODO Remove side effect or change login to void
    @Override
    public boolean login(String email, String password) throws WrongCredentialsException, CrudException {
        ArrayList<Company> allCompanies = companiesDAO.getAllCompanies();
        for (Company company :
                allCompanies) {
            if (email.equalsIgnoreCase(company.getEmail()) && String.valueOf(password.hashCode()).equals(company.getPassword())) {
                companyId = company.getId();
                return true;
            }
        }
        throw new WrongCredentialsException("Wrong email or password");
    }

    /**
     * Adds a new coupon to the database with the logged in company id.
     *
     * @param coupon A coupon object (id param is not required)
     * @return The generated coupon id
     * @throws CouponTitleExistException If the coupon title already exist
     * @throws CrudException             If there is any SQL exception
     * @see Coupon
     */
    public long addCoupon(Coupon coupon) throws CouponTitleExistException, CrudException {
        coupon.setCompanyID((int) companyId);//TODO: make all ids Long
        ArrayList<Coupon> allCoupons = couponsDAO.getAllCoupons();
        for (Coupon c :
                allCoupons) {
            if (coupon.getCompanyID() == c.getCompanyID() && c.getTitle().equalsIgnoreCase(coupon.getTitle())) {
                throw new CouponTitleExistException();
            }
            return couponsDAO.addCoupon(coupon);
        }
        return -1;
    }

    /**
     * Updates an existing coupon of the logged in company
     * Company id cannot be changed
     *
     * @param coupon A full coupon object (id param is required)
     * @throws UpdateException If the coupon company_id not equals to the logged in company
     * @throws UpdateException If the company trying to change the company_id in the database
     * @throws CrudException   If there is any SQL exception
     * @see Coupon
     */
    public void updateCoupon(Coupon coupon) throws UpdateException, CrudException {
        if (companyId != coupon.getCompanyID()) {
            throw new UpdateException("This coupon is not belongs the logged in company your company\n" +
                    "Coupon company_id = " + coupon.getCompanyID() +
                    "\n" +
                    "Logged in company id = " + companyId);
        }
        Coupon couponToUpdate = couponsDAO.getOneCoupon(coupon.getId());
        if (coupon.getCompanyID() != couponToUpdate.getCompanyID()) {
            throw new UpdateException("Company ID cannot be changed");
        }
        couponsDAO.updateCoupon(coupon);
    }

    /**
     * Deletes coupon and all it's purchases from the database
     *
     * @param couponId The coupon id in the database
     * @throws CrudException If there is any SQL exception
     */
    public void deleteCoupon(int couponId) throws CrudException {
        couponsDAO.deletePurchaseByCouponId(couponId);
        couponsDAO.deleteCoupon(couponId);
    }

    /**
     * Gets all the coupons of the  logged in company
     *
     * @return ArrayList of the company's coupons
     * @throws CrudException If there is any SQL exception
     * @see Company
     */
    public ArrayList<Coupon> getCompanyCoupons() throws CrudException {
        return couponsDAO.getCompanyCoupons(companyId);
    }

    /**
     * Gets all the coupons of the  logged in company filtered by category
     *
     * @param category Category (enum) to filter the result
     * @return ArrayList of the company's coupons filtered by given category
     * @throws CrudException If there is any SQL exception
     * @see Company
     */
    public ArrayList<Coupon> getCompanyCoupons(Category category) throws CrudException {
        return couponsDAO.getCompanyCoupons(companyId, category);
    }

    /**
     * Gets all the coupons of the  logged in company up to max price
     *
     * @param maxPrice The price to filter the result
     * @return ArrayList of the company's coupons up to hte given maximum price
     * @throws CrudException If there is any SQL exception
     * @see Company
     */
    public ArrayList<Coupon> getCompanyCoupons(double maxPrice) throws CrudException {
        return couponsDAO.getCompanyCoupons(companyId, maxPrice);
    }

    /**
     * Gets the logged in company details
     *
     * @return Company object
     * @throws CrudException If there is any SQL exception
     * @see Company
     */
    public Company loggedInCompanyDetails() throws CrudException {
        Company company = companiesDAO.getOneCompany(companyId);
        company.setCoupons(getCompanyCoupons());
        return company;
    }
}
