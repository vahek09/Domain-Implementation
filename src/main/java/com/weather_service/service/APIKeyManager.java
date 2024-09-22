package com.weather_service.service;

import com.weather_service.domain.User;
import com.weather_service.repository.UserRepository;

import java.util.UUID;

public class APIKeyManager {
//    private UserRepository userRepository;
//
//    public APIKeyManager(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public void assignAPIKey(User user) {
//        if (user == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//        String apiKey = generateAPIKey();
//        user.setApiKey(apiKey);
//        userRepository.updateUser(user);
//    }
//
//    // Method to cancel an API key from a user
//    public void cancelAPIKey(User user) {
//        if (user == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//        user.setApiKey(null);
//        userRepository.updateUser(user);
//    }
//
//    private String generateAPIKey() {
//        return UUID.randomUUID().toString();
//    }
}
