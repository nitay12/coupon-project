package com.nitay.couponproject.utils;

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
            e.printStackTrace();
            return null;
        }
    }

    public static Integer getIntInput(String message) {
        try {
            System.out.println(message);
            return new Scanner(System.in).nextInt();
        } catch (InputMismatchException e) {
            e.printStackTrace();
            return null;
        }
    }
}
