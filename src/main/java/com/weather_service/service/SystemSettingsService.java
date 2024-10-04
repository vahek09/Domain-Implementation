package com.weather_service.service;

import com.weather_service.service.core.UserService;
import com.weather_service.domain.User;
import java.util.Scanner;

public class SystemSettingsService {
    private final SettingsManager settingsManager;
    private final UserService userService;

    public SystemSettingsService(SettingsManager settingsManager, UserService userService) {
        this.settingsManager = settingsManager;
        this.userService = userService;
    }

    public void manageSystemSettings(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        User user = userService.findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter new language: ");
        String newLanguage = scanner.nextLine();

        System.out.print("Enter new units (metric/imperial): ");
        String newUnits = scanner.nextLine();

        settingsManager.updateSettings(user, newLanguage, newUnits);
        System.out.println("User settings updated.");
    }
}
