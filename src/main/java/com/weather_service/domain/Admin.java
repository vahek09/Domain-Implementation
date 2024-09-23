package com.weather_service.domain;

import com.weather_service.service.APIKeyManager;
import com.weather_service.service.SettingsManager;
import com.weather_service.service.UserService;

public class Admin extends User {

    public Admin() {
        super("defaultID", "defaultUsername", "defaultLocation", "metric", "en", true, false, true, "defaultAPIKey");
    }

    public Admin(String userID, String username, String location, String preferredUnits, String preferredLanguage, String apiKey) {
        super(userID, username, location, preferredUnits, preferredLanguage, true, false, true, apiKey);
    }


    public void manageAPIKeys(UserService userService, User user, APIKeyManager apiKeyManager, boolean assign) {
        if (assign) {
            apiKeyManager.assignAPIKey(user);
        } else {
            apiKeyManager.cancelAPIKey(user);
        }
        userService.updateUser(user);
    }

    public void viewUsageStatistics(UserService userService) {
        System.out.println("User Usage Statistics:");
        for (User user : userService.listAllUsers()) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("API Key: " + user.getApiKey());
            System.out.println("Preferred Units: " + user.getPreferredUnits());
            System.out.println("Alerts Enabled: " + user.isAlertsEnabled());
            System.out.println("API Call Count: " + user.getApiCallCount());
            System.out.println("Last API Call: " + user.getLastApiCallTime());
            System.out.println("-----------------------------");
        }
    }

    public void manageSystemSettings(SettingsManager settingsManager, User user, String newLanguage, String newUnits, boolean alertsEnabled) {
        settingsManager.updateSettings(user, newLanguage, newUnits, alertsEnabled);
    }

    public void createUser(UserService userService, String userId, String username, String location, boolean isPremium) {
        userService.createUser(userId, username, location, isPremium);
        System.out.println("Admin created a new user: " + username);
    }

    public void deleteUser(UserService userService, String userId) {
        userService.deleteUser(userId);
        System.out.println("Admin deleted user with ID: " + userId);
    }
}
