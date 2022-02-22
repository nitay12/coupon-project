package com.nitay.couponproject.facades;

import com.nitay.couponproject.exceptions.CouponTitleExistException;
import com.nitay.couponproject.exceptions.UpdateException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.model.Category;
import com.nitay.couponproject.model.Company;
import com.nitay.couponproject.model.Coupon;

import java.util.ArrayList;
//TODO: Verify that methods using companyId arent get null or 0 (throw not logged in exception)
public class CompanyFacade extends ClientFacade {
    @Override
    protected boolean login(String email, String password) throws WrongCredentialsException {
        ArrayList<Company> allCompanies = companiesDBDAO.getAllCompanies();
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
    public long addCoupon(Coupon coupon) throws CouponTitleExistException {
        ArrayList<Coupon> allCoupons = couponsDBDAO.getAllCoupons();
        for (Coupon c :
                allCoupons) {
            if (coupon.getCompanyID() == c.getCompanyID() && c.getTitle().equalsIgnoreCase(coupon.getTitle())) {
                throw new CouponTitleExistException();
            }
            return couponsDBDAO.addCoupon(coupon);
        }
        return -1;
    }
    //TODO: Verify that company id is equal to companyId state variable
    public void updateCoupon(Coupon coupon) throws UpdateException {
        Coupon couponToUpdate = couponsDBDAO.getOneCoupon(coupon.getId());
        if(coupon.getCompanyID()==couponToUpdate.getCompanyID()){
            throw new UpdateException("Company ID cannot be updated");
        }
        couponsDBDAO.updateCoupon(coupon);
    }
    public void deleteCoupon(int couponId){
        couponsDBDAO.deleteCoupon(couponId);
        couponsDBDAO.deleteCouponPurchase(couponId);
    }
    public ArrayList<Coupon> getCompanyCoupons(){
        return couponsDBDAO.getCompanyCoupons(companyId);
    }
    public ArrayList<Coupon> getCompanyCouponsByCategory(Category category){
        return couponsDBDAO.getCompanyCoupons(companyId, category);
    }
    public ArrayList<Coupon> getCompanyCouponsMaxPrice(double maxPrice){
        return couponsDBDAO.getCompanyCoupons(companyId, maxPrice);
    }
    public Company loggedInCompanyDetails(){
        return companiesDBDAO.getOneCompany(companyId);
    }
}
