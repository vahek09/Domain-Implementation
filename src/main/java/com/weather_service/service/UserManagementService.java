package com.weather_service.service;

import com.weather_service.domain.Admin;
import com.weather_service.service.core.UserService;
import com.weather_service.domain.User;
import com.weather_service.domain.Location;
import com.weather_service.utility.InputUtility;
import java.util.Scanner;

public class UserManagementService {
    private final UserService userService;

    public UserManagementService(UserService userService) {
        this.userService = userService;
    }

    public void deleteUser(Scanner scanner, Admin loggedInAdmin) {
        User user = InputUtility.findUserByIdOrUsername(scanner, userService);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        if (user.getUserID().equals(loggedInAdmin.getUserID())) {
            System.out.println("You cannot delete the admin user you are currently logged in with.");
            return;
        }

        userService.deleteUser(user.getUserID());
        System.out.println("User deleted: " + user.getUsername());
    }

    public void createUser(Scanner scanner) {
        System.out.println("Creating a new user");

        String userId = InputUtility.getValidUserId(scanner);
        String username = InputUtility.getValidUsername(scanner);
        String cityName = InputUtility.getCityName(scanner);

        double latitude = InputUtility.getValidDouble(scanner, "Enter latitude: ");
        double longitude = InputUtility.getValidDouble(scanner, "Enter longitude: ");

        System.out.print("Is this user an Admin? (true/false): ");
        boolean isAdmin = scanner.nextBoolean();
        scanner.nextLine();

        Location location = new Location(latitude, longitude, cityName);
        userService.createUser(userId, username, location, isAdmin);
    }

    public void viewAllUsers() {
        var users = userService.listAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(user -> System.out.println(user.getUsername() + " (ID: " + user.getUserID() + ")"));
        }
    }
}
