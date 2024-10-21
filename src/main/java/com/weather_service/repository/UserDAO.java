package com.weather_service.repository;

import com.weather_service.domain.User;
import com.weather_service.domain.WeatherData;
import com.weather_service.utility.JdbcUtility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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


    // Without Transaction
    public void insertUserAndWeatherDataWithoutTransaction(User user, WeatherData weatherData, int locationId) {
        String insertUserQuery = "INSERT INTO users (user_id, username, is_admin, api_key, api_call_count, last_api_call_time) VALUES (?, ?, ?, ?, ?, ?)";
        String insertWeatherDataQuery = "INSERT INTO weather_data (location_id, user_id, temperature, feels_like, temp_min, temp_max, humidity, pressure, wind_speed, wind_degree, visibility, rain_1h, cloudiness, timestamp, weather_description, weather_icon) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Insert user
            jdbcUtility.execute(insertUserQuery, user.getUserID(), user.getUsername(), user.isAdmin(), user.getApiKey(), user.getApiCallCount(), user.getLastApiCallTime());

            // Insert weather data
            jdbcUtility.execute(insertWeatherDataQuery, locationId, user.getUserID(), weatherData.getTemperature(), weatherData.getFeelsLike(), weatherData.getTempMin(), weatherData.getTempMax(),
                    weatherData.getHumidity(), weatherData.getPressure(), weatherData.getWindSpeed(), weatherData.getWindDegree(),
                    weatherData.getVisibility(), weatherData.getRain1h(), weatherData.getCloudiness(), weatherData.getTimestamp(),
                    weatherData.getWeatherDescription(), weatherData.getWeatherIcon());

            // Simulating failure after weather data insertion to demonstrate an inconsistent state
            throw new SQLException("Simulated failure after user and weather data insertion");

        } catch (SQLException e) {
            // No rollback here, so the User will be inserted but WeatherData will not
            e.printStackTrace();
        }
    }


    public void insertUserAndWeatherDataWithTransaction(User user, WeatherData weatherData, int locationId) {
        String insertUserQuery = "INSERT INTO users (user_id, username, is_admin, api_key, api_call_count, last_api_call_time) VALUES (?, ?, ?, ?, ?, ?)";
        String insertWeatherDataQuery = "INSERT INTO weather_data (location_id, user_id, temperature, feels_like, temp_min, temp_max, humidity, pressure, wind_speed, wind_degree, visibility, rain_1h, cloudiness, timestamp, weather_description, weather_icon) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        try {
            connection = jdbcUtility.getConnection();
            connection.setAutoCommit(false);  // Begin the transaction

            jdbcUtility.execute(connection, insertUserQuery, user.getUserID(), user.getUsername(), user.isAdmin(), user.getApiKey(), user.getApiCallCount(), user.getLastApiCallTime());

            jdbcUtility.execute(connection, insertWeatherDataQuery, locationId, user.getUserID(), weatherData.getTemperature(), weatherData.getFeelsLike(), weatherData.getTempMin(), weatherData.getTempMax(),
                    weatherData.getHumidity(), weatherData.getPressure(), weatherData.getWindSpeed(), weatherData.getWindDegree(),
                    weatherData.getVisibility(), weatherData.getRain1h(), weatherData.getCloudiness(), weatherData.getTimestamp(),
                    weatherData.getWeatherDescription(), weatherData.getWeatherIcon());

            connection.commit();
            System.out.println("Transaction committed successfully");

            throw new SQLException("Simulated failure after user and weather data insertion");

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.out.println("Rolling back due to error...");
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

