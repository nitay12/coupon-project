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
        coupon.setCompanyID((int) companyId);//TODO: make all ids Long
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
        if(coupon.getCompanyID()!=couponToUpdate.getCompanyID()){
            throw new UpdateException("Company ID cannot be updated");
        }
        couponsDBDAO.updateCoupon(coupon);
    }
    public void deleteCoupon(int couponId){
        couponsDBDAO.deleteCouponPurchase(couponId);
        couponsDBDAO.deleteCoupon(couponId);
    }
    public ArrayList<Coupon> getCompanyCoupons(){
        return couponsDBDAO.getCompanyCoupons(companyId);
    }
    public ArrayList<Coupon> getCompanyCoupons(Category category){
        return couponsDBDAO.getCompanyCoupons(companyId, category);
    }
    public ArrayList<Coupon> getCompanyCoupons(double maxPrice){
        return couponsDBDAO.getCompanyCoupons(companyId, maxPrice);
    }
    public Company loggedInCompanyDetails() throws CrudException {
        return companiesDBDAO.getOneCompany(companyId);
    }
}
