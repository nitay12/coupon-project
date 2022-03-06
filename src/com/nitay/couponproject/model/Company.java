package com.nitay.couponproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@AllArgsConstructor()
@NoArgsConstructor()
@Data
@ToString
public class Company {
    private long id;
    private String name;
    private String email;
    private String password;
    private ArrayList<Coupon> coupons;

    public Company(long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
