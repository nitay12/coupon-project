package com.nitay.couponproject.utils;

import com.nitay.couponproject.model.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * An input utility class
 * Used to get inputs from the console (simplifies the using of Scanner and System.out)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyScanner {
    /**
     * Gets a string input, if the input mismatch the method calls itself again with error message
     *
     * @param message The input message
     * @return The user string input
     */
    public static String getStringInput(String message) {
        try {
            System.out.print(message + ": ");
            return new Scanner(System.in).nextLine();
        } catch (InputMismatchException e) {
            return getStringInput("Wrong input please try again");
        }
    }
    public static String getStringInput(String message , List<String> validStrings) {
        try {
            System.out.print(message + " | valid answers: "+validStrings.toString() + ": ");
            String input = new Scanner(System.in).nextLine();
            for (String validString :
                    validStrings) {
                if (validString.equalsIgnoreCase(input)){
                    return input;
                }
            }
            throw new InputMismatchException();
        } catch (InputMismatchException e) {
            return getStringInput("Wrong input please try again", validStrings);
        }
    }

    /**
     * Gets a integer input, if the input mismatch the method calls itself again with error message
     *
     * @param message The input message
     * @return The user int input
     */
    public static Integer getIntInput(String message) {
        try {
            System.out.print(message + ": ");
            return new Scanner(System.in).nextInt();
        } catch (InputMismatchException e) {
            return getIntInput("Wrong input please try again");
        }
    }

    /**
     * Gets a double input, if the input mismatch the method calls itself again with error message
     *
     * @param message The input message
     * @return The user double input
     */
    public static Double getDoubleInput(String message) {
        try {
            System.out.print(message + ": ");
            return new Scanner(System.in).nextDouble();
        } catch (InputMismatchException e) {
            return getDoubleInput("Wrong input please try again");
        }
    }

    /**
     * Gets a Category input.
     * If the input not match to the Category enum values the method calls itself again with error message and prints the available categories.
     * If the input mismatch the method calls itself again with error message.
     *
     * @param message The input message
     * @return a Category (enum)
     * @see Category
     */
    public static Category getCategory(String message) {
        try {
            System.out.print(message + ": ");
            return Category.valueOf(new Scanner(System.in).next());
        } catch (InputMismatchException e) {
            return getCategory("Wrong input please try again");
        } catch (IllegalArgumentException e) {
            System.out.print("Category not found, The available categories are:\n ");
            printAvailableCategories();
            return getCategory("Please enter a valid category name");
        }
    }

    private static void printAvailableCategories() {
        for (Category category : Category.values()
        ) {
            System.out.println(category.name());
        }
    }
}