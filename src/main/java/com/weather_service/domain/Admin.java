package com.weather_service.domain;

public class Admin extends User {
    public Admin(String userID, String username, Location location, String preferredUnits, String preferredLanguage, Boolean isAdmin, String apiKey) {
        super(userID, username, location, preferredUnits, preferredLanguage, isAdmin, apiKey);
    }
}
