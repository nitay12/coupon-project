package com.nitay.couponproject.utils;

import com.nitay.couponproject.model.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.InputMismatchException;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyScanner {
    public static String getStringInput(String message) {
        try {
            System.out.print(message);
            return new Scanner(System.in).next();
        } catch (InputMismatchException e) {
            System.out.println("Wrong input please try again");
            return getStringInput(message);
        }
    }

    public static Integer getIntInput(String message) {
        try {
            System.out.println(message);
            return new Scanner(System.in).nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Wrong input please try again");
            return getIntInput(message);
        }
    }

    public static Double getDoubleInput(String message) {
        try {
            System.out.println(message);
            return new Scanner(System.in).nextDouble();
        } catch (InputMismatchException e) {
            return getDoubleInput("Wrong input please try again: ");
        }
    }
    public static Category getCategory(String message){
        try{
            System.out.println(message);
            return Category.valueOf(new Scanner(System.in).next());
        }
        catch (InputMismatchException e){
            return getCategory("Wrong input please try again: ");
        }
    }
}
