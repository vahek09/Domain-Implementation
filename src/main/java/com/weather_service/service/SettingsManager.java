package com.weather_service.service;

import com.weather_service.service.core.UserService;
import com.weather_service.domain.SystemSettings;
import com.weather_service.domain.User;
import com.weather_service.interfaces.SettingsManagerInterface;

public class SettingsManager implements SettingsManagerInterface {

    private final UserService userService;

    public SettingsManager(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("UserService cannot be null");
        }
        this.userService = userService;
    }

    @Override
    public SystemSettings getSystemSettings(User user) {
        return new SystemSettings(user.getPreferredLanguage(), user.getPreferredUnits());
    }

    @Override
    public void updateSettings(User user, String newLanguage, String newUnits) {
        user.setPreferredLanguage(newLanguage);
        user.setPreferredUnits(newUnits);

        userService.updateUser(user);
    }
}
