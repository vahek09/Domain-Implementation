package com.weather_service.service;

import com.weather_service.domain.SystemSettings;
import com.weather_service.domain.User;

public class SettingsManager {

    private final UserService userService;

    // Constructor to inject the UserService
    public SettingsManager(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("UserService cannot be null");
        }
        this.userService = userService;
    }

    // Retrieve system settings for a user
    public SystemSettings getSystemSettings(User user) {
        return new SystemSettings(user.getPreferredLanguage(), user.getPreferredUnits(), user.isAlertsEnabled());
    }

    // Update settings for a user
    public void updateSettings(User user, String newLanguage, String newUnits, boolean alertsEnabled) {
        user.setPreferredLanguage(newLanguage);
        user.setPreferredUnits(newUnits);
        user.setAlertsEnabled(alertsEnabled);

        // Use UserService to update the user
        userService.updateUser(user);
    }
}
