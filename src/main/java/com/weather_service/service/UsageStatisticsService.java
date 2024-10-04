package com.weather_service.service;

import com.weather_service.service.core.UserService;
import com.weather_service.domain.User;

public class UsageStatisticsService {
    private final UserService userService;

    public UsageStatisticsService(UserService userService) {
        this.userService = userService;
    }

    public void viewUsageStatistics() {
        System.out.println("User Usage Statistics:");
        for (User user : userService.listAllUsers()) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("API Key: " + user.getApiKey());
            System.out.println("Preferred Units: " + user.getPreferredUnits());
            System.out.println("API Call Count: " + user.getApiCallCount());
            System.out.println("Last API Call: " + user.getLastApiCallTime());
            System.out.println("-----------------------------");
        }
    }
}
