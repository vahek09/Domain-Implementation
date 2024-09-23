package com.weather_service.repository;

import com.weather_service.domain.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private Map<String, User> usersById = new HashMap<>();
    private Map<String, User> usersByUsername = new HashMap<>();

    public User findById(String userId) {
        return usersById.get(userId);
    }

    public User findByUsername(String username) {
        return usersByUsername.get(username);
    }

    public Collection<User> findAll() {
        return usersById.values();
    }

    public void saveUser(User user) {
        usersById.put(user.getUserID(), user);
        usersByUsername.put(user.getUsername(), user);
    }

    public void deleteUser(String userId) {
        User user = usersById.remove(userId);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
        }
    }

    public Collection<User> findAllByFilter(boolean isPremium) {
        return usersById.values().stream()
                .filter(user -> user.isPremium() == isPremium)
                .toList();
    }
}
