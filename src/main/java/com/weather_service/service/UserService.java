package com.weather_service.service;

import com.weather_service.domain.User;
import com.weather_service.repository.UserRepository;

import java.util.Collection;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public void createUser(String userId, String username, String location, boolean isPremium) {
        User user = new User(userId, username, location, "metric", "en", isPremium,false, true, null);
        userRepository.saveUser(user);
        System.out.println("User created: " + username);
    }

    public void updateUser(User user) {
        userRepository.saveUser(user);
        System.out.println("User updated: " + user.getUsername());
    }

    public User findUser(String userId) {
        return userRepository.findById(userId);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Collection<User> listAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String userId) {
        userRepository.deleteUser(userId);
        System.out.println("User with ID: " + userId + " deleted.");
    }

    public Collection<User> listPremiumUsers() {
        return userRepository.findAllByFilter(true);
    }

    public void changeLanguage(User user, String newLanguage) {
        user.setPreferredLanguage(newLanguage);
    }
}
