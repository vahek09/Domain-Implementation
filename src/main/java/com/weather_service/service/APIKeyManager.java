package com.weather_service.service;

import com.weather_service.domain.User;

import java.util.UUID;

public class APIKeyManager {
    private final UserService userService;

    public APIKeyManager(UserService userService) {
        this.userService = userService;
    }

    public void assignAPIKey(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        String apiKey = generateAPIKey();
        user.setApiKey(apiKey);
        userService.updateUser(user);
        System.out.println("Assigned API key to user: " + user.getUsername());
    }

    public void cancelAPIKey(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        user.setApiKey(null);
        userService.updateUser(user);
        System.out.println("Canceled API key for user: " + user.getUsername());
    }

    private String generateAPIKey() {
        return UUID.randomUUID().toString();
    }
}
