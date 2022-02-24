package com.nitay.couponproject.utils;

import com.nitay.couponproject.dal.CompaniesDBDAO;
import com.nitay.couponproject.enums.ClientType;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.facades.AdminFacade;
import com.nitay.couponproject.facades.ClientFacade;
import com.nitay.couponproject.facades.CompanyFacade;
import com.nitay.couponproject.facades.CustomerFacade;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginManager {
    @Getter
    private static LoginManager instance = new LoginManager();

    public ClientFacade login(String email, String password, ClientType clientType) throws WrongCredentialsException {
        switch (clientType) {
            case Administrator -> {
                AdminFacade adminFacade = new AdminFacade();
                if (adminFacade.login(email, password)) {
                    return adminFacade;

                }
            }
            case Company -> {
                CompanyFacade companyFacade = new CompanyFacade();
                if (companyFacade.login(email, password)) {
                    return companyFacade;

                }
            }
            case Customer -> {
                CustomerFacade customerFacade = new CustomerFacade();
                if (customerFacade.login(email, password)) {
                    return customerFacade;
                }
            }
        }
        return null;
    }
}
