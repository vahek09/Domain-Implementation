package com.weather_service.service.core;

import com.weather_service.domain.Location;
import com.weather_service.domain.User;
import com.weather_service.interfaces.UserServiceInterface;
import com.weather_service.repository.UserRepository;

import java.util.Collection;

public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(String userId, String username, Location location, boolean isAdmin) {
        if (userRepository.userExistsById(userId)) {
            System.out.println("Error: A user with this ID already exists.");
            return;
        }
        if (userRepository.userExistsByUsername(username)) {
            System.out.println("Error: A user with this username already exists.");
            return;
        }

        User user = new User(userId, username, location, "metric", "en", isAdmin, null);
        userRepository.saveUser(user);
        System.out.println("User created: " + username);
    }

    @Override
    public void updateUser(User user) {
        userRepository.saveUser(user);
        System.out.println("User updated: " + user.getUsername());
    }

    @Override
    public User findUser(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Collection<User> listAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteUser(userId);
        System.out.println("User with ID: " + userId + " deleted.");
    }
}
