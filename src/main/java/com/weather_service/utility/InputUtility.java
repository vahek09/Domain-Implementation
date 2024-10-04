package com.weather_service.utility;

import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;

import java.util.Scanner;

public class InputUtility {

    public static String getValidUserId(Scanner scanner) {
        String userId;
        while (true) {
            System.out.print("Enter User ID (3 digits only): ");
            userId = scanner.nextLine();

            if (userId.matches("\\d{3}")) {
                break;
            } else {
                System.out.println("Invalid User ID. Please enter exactly 3 digits.");
            }
        }
        return userId;
    }

    public static String getValidUsername(Scanner scanner) {
        String username;
        while (true) {
            System.out.print("Enter Username (letters only): ");
            username = scanner.nextLine();

            if (username.matches("[a-zA-Z]+")) {
                break;
            } else {
                System.out.println("Invalid Username. Please enter letters only.");
            }
        }
        return username;
    }

    public static String getCityName(Scanner scanner) {
        System.out.print("Enter city name: ");
        return scanner.nextLine();
    }

    public static double getValidDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                return scanner.nextDouble();
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();  // Consume invalid input
            }
        }
    }

    public static User findUserByIdOrUsername(Scanner scanner, UserService userService) {
        System.out.println("Would you like to search by:");
        System.out.println("1. User ID");
        System.out.println("2. Username");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        User user = null;
        if (choice == 1) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            user = userService.findUser(userId);
        } else if (choice == 2) {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            user = userService.findUserByUsername(username);
        } else {
            System.out.println("Invalid option. Please try again.");
        }

        return user;
    }


}
