package com.weather_service.service;

import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;

import java.util.Scanner;

public class APIKeyManagementService {
    private final APIKeyManager apiKeyManager;
    private final UserService userService;

    public APIKeyManagementService(APIKeyManager apiKeyManager, UserService userService) {
        this.apiKeyManager = apiKeyManager;
        this.userService = userService;
    }

    public void manageAPIKeys(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        User user = userService.findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Current API Key: " + (user.getApiKey() != null ? user.getApiKey() : "No API key assigned"));

        System.out.println("Would you like to:");
        System.out.println("1. Assign API key");
        System.out.println("2. Delete API key");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1 && user.getApiKey() == null) {
            apiKeyManager.assignAPIKey(user);
            System.out.println("API key assigned to user: " + user.getUsername());
        } else if (choice == 2 && user.getApiKey() != null) {
            apiKeyManager.revokeAPIKey(user);
            System.out.println("API key deleted for user: " + user.getUsername());
        } else {
            System.out.println("Invalid action.");
        }
    }
}
