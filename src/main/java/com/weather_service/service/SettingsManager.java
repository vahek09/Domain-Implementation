package com.weather_service.service;

import com.weather_service.domain.SystemSettings;
import com.weather_service.domain.User;
import com.weather_service.repository.UserRepository;

public class SettingsManager {

//    private UserRepository userRepository;
//
//    // Constructor to inject the UserRepository
//    public SettingsManager(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public SystemSettings getSystemSettings(User user) {
//        return new SystemSettings(user.getPreferredLanguage(), user.getPreferredUnits(), user.isAlertsEnabled());
//    }
//
//    public void updateSettings(User user, String newLanguage, String newUnits, boolean alertsEnabled) {
//        user.setPreferredLanguage(newLanguage);
//        user.setPreferredUnits(newUnits);
//        user.setAlertsEnabled(alertsEnabled);
//        userRepository.updateUser(user);
//    }
}
