package com.weather_service.interfaces;

import com.weather_service.domain.Location;
import com.weather_service.domain.User;

import java.util.Collection;

public interface UserServiceInterface {
    void createUser(String userId, String username, Location location, boolean isAdmin);

    void updateUser(User user);

    User findUser(String userId);

    User findUserByUsername(String username);

    Collection<User> listAllUsers();

    void deleteUser(String userId);
}
