package com.weather_service.repository;

import com.weather_service.domain.User;
import com.weather_service.utility.JdbcUtility;

import java.sql.ResultSet;
import java.util.List;

public class UserDAO {

    private final JdbcUtility jdbcUtility;

    public UserDAO(JdbcUtility jdbcUtility) {
        this.jdbcUtility = jdbcUtility;
    }

    public void createUser(User user) {
        String query = "INSERT INTO users (user_id, username, is_admin, api_key, api_call_count, last_api_call_time) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcUtility.execute(query, user.getUserID(), user.getUsername(), user.isAdmin(), user.getApiKey(), user.getApiCallCount(), user.getLastApiCallTime());
    }

    public User findById(String userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        return jdbcUtility.findOne(query, this::mapRowToUser, userId);
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        return jdbcUtility.findMany(query, this::mapRowToUser);
    }

    public void updateUser(User user) {
        String query = "UPDATE users SET username = ?, is_admin = ?, api_key = ?, api_call_count = ?, last_api_call_time = ? WHERE user_id = ?";
        jdbcUtility.execute(query, user.getUsername(), user.isAdmin(), user.getApiKey(), user.getApiCallCount(), user.getLastApiCallTime(), user.getUserID());
    }

    public void deleteUser(String userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        jdbcUtility.execute(query, userId);
    }

    // Map a ResultSet row to a User object
    private User mapRowToUser(ResultSet rs) {
        try {
            return new User(
                    rs.getString("user_id"),
                    rs.getString("username"),
                    null,  // Location will be fetched separately
                    null,  // preferredUnits will be fetched separately
                    null,  // preferredLanguage will be fetched separately
                    rs.getBoolean("is_admin"),
                    rs.getString("api_key")
            );
        } catch (Exception e) {
            throw new RuntimeException("Error mapping row to User", e);
        }
    }
}
