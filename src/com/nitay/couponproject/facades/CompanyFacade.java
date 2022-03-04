package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.CouponTitleExistException;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.UpdateException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
//TODO: Verify that methods using companyId arent get null or 0 (throw not logged in exception)
@AllArgsConstructor
@NoArgsConstructor
public class CompanyFacade extends ClientFacade {
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
    private long companyId;
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
    //TODO: Verify that company id is equal to companyId state variable
    public void updateCoupon(Coupon coupon) throws UpdateException, CrudException {
        Coupon couponToUpdate = couponsDAO.getOneCoupon(coupon.getId());
        if(coupon.getCompanyID()!=couponToUpdate.getCompanyID()){
            throw new UpdateException("Company ID cannot be updated");
        }
        couponsDAO.updateCoupon(coupon);
    }
    public void deleteCoupon(int couponId) throws CrudException {
        couponsDAO.deletePurchaseByCouponId(couponId);
        couponsDAO.deleteCoupon(couponId);
    }
    public ArrayList<Coupon> getCompanyCoupons() throws CrudException {
        return couponsDAO.getCompanyCoupons(companyId);
    }
    public ArrayList<Coupon> getCompanyCoupons(Category category) throws CrudException {
        return couponsDAO.getCompanyCoupons(companyId, category);
    }
    public ArrayList<Coupon> getCompanyCoupons(double maxPrice) throws CrudException {
        return couponsDAO.getCompanyCoupons(companyId, maxPrice);
    }
    public Company loggedInCompanyDetails() throws CrudException {
        return companiesDAO.getOneCompany(companyId);
    }
}
