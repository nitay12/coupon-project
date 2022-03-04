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
            return getStringInput("Wrong input please try again: ");
        }
    }

    public static Integer getIntInput(String message) {
        try {
            System.out.print(message);
            return new Scanner(System.in).nextInt();
        } catch (InputMismatchException e) {
            return getIntInput("Wrong input please try again: ");
        }
    }

    public static Double getDoubleInput(String message) {
        try {
            System.out.print(message);
            return new Scanner(System.in).nextDouble();
        } catch (InputMismatchException e) {
            return getDoubleInput("Wrong input please try again: ");
        }
    }

    public static Category getCategory(String message) {
        try {
            System.out.print(message);
            return Category.valueOf(new Scanner(System.in).next());
        } catch (InputMismatchException e) {
            return getCategory("Wrong input please try again: ");
        } catch (IllegalArgumentException e) {
            System.out.print("Category not found, The available categories are: ");
            printCategories();
            return getCategory("Please enter a valid category name: ");
        }
    }

    private static void printCategories() {
        for (Category category : Category.values()
        ) {
            System.out.println(category);
        }
    }
}
