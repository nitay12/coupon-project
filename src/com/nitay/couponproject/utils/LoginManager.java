package com.nitay.couponproject.utils;

import com.nitay.couponproject.enums.ClientType;
import com.nitay.couponproject.exceptions.CrudException;
import com.nitay.couponproject.exceptions.WrongCredentialsException;
import com.nitay.couponproject.facades.AdminFacade;
import com.nitay.couponproject.facades.ClientFacade;
import com.nitay.couponproject.facades.CompanyFacade;
import com.nitay.couponproject.facades.CustomerFacade;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A login utility singleton class.
 * Operates the login methods for each client type and returns the appropriate facade with the logged in client id in it (as a property).
 * Used for authentication and security so the access to the facades must go through it.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginManager {
    @Getter
    private static final LoginManager instance = new LoginManager();

    /**
     * The main login method
     *
     * @param email      The client email
     * @param password   The client password
     * @param clientType the client type (enum)
     * @return the appropriate logged in client facade | return null if the login failed
     * @throws WrongCredentialsException If the email/password not match
     * @throws CrudException             For any SQL exception
     * @see ClientType
     * @see ClientFacade
     * @see CrudException
     * @see WrongCredentialsException
     */
    public ClientFacade login(String email, String password, ClientType clientType) throws WrongCredentialsException, CrudException, InterruptedException {
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
