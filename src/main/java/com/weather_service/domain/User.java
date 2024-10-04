package com.weather_service.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private String userID;
    private String username;
    private Location location;
    private String preferredUnits;
    private String preferredLanguage;
    private boolean isAdmin;
    private String apiKey;
    private int apiCallCount;
    private LocalDateTime lastApiCallTime;

    public User(String userID, String username, Location location, String preferredUnits, String preferredLanguage, boolean isAdmin, String apiKey) {
        this.userID = userID;
        this.username = username;
        this.location = location;
        this.preferredUnits = preferredUnits;
        this.preferredLanguage = preferredLanguage;
        this.isAdmin = isAdmin;
        this.apiKey = apiKey;
        this.apiCallCount = 0;
        this.lastApiCallTime = null;
    }

    public void recordApiCall() {
        apiCallCount++;
        lastApiCallTime = LocalDateTime.now();
    }
}
