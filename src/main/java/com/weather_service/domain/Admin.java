package com.weather_service.domain;

import com.weather_service.repository.UserRepository;
import com.weather_service.service.APIKeyManager;
import com.weather_service.service.SettingsManager;

public class Admin extends User {

//    public Admin() {
//        super("defaultID", "defaultUsername", "defaultLocation", "metric", "en", false, true, "defaultAPIKey");
//    }
//    public Admin(String userID, String username, String location, String preferredUnits, String preferredLanguage, String apiKey) {
//        super(userID, username, location, preferredUnits, preferredLanguage, true, true, apiKey);
//    }
//
//    public void manageAPIKeys(User user, APIKeyManager apiKeyManager, boolean assign) {
//        if (assign) {
//            apiKeyManager.assignAPIKey(user);
//        } else {
//            apiKeyManager.cancelAPIKey(user);
//        }
//    }
//
//    public void viewUsageStatistics(UserRepository userRepository) {
//        System.out.println("User Usage Statistics:");
//        for (User user : userRepository.findAll()) {
//            System.out.println("Username: " + user.getUsername());
//            System.out.println("API Key: " + user.getApiKey());
//            System.out.println("Preferred Units: " + user.getPreferredUnits());
//            System.out.println("Alerts Enabled: " + user.isAlertsEnabled());
//             System.out.println("API Call Count: " + user.getApiCallCount());
//             System.out.println("Last API Call: " + user.getLastApiCallTime());
//            System.out.println("-----------------------------");
//        }
//    }
//
//    public void manageSystemSettings(SettingsManager settingsManager, User user, String newLanguage, String newUnits, boolean alertsEnabled) {
//        settingsManager.updateSettings(user, newLanguage, newUnits, alertsEnabled);
//    }
}
