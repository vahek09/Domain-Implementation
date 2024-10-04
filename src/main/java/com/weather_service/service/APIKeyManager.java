package com.weather_service.service;

import com.weather_service.service.core.UserService;
import com.weather_service.domain.User;
import com.weather_service.interfaces.APIKeyManagerInterface;
import io.github.cdimascio.dotenv.Dotenv;

public class APIKeyManager  implements APIKeyManagerInterface {
    private final UserService userService;
    private static final String API_KEY_ENV = "OPENWEATHER_API_KEY";

    public APIKeyManager(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void assignAPIKey(User user) {
        Dotenv dotenv = Dotenv.load();
        String sharedApiKey = dotenv.get(API_KEY_ENV);

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        user.setApiKey(sharedApiKey);
        userService.updateUser(user);
        System.out.println("Assigned API key to user: " + user.getUsername());
    }

    @Override
    public void revokeAPIKey(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        user.setApiKey(null);
        userService.updateUser(user);
        System.out.println("Canceled API key for user: " + user.getUsername());
    }

}
