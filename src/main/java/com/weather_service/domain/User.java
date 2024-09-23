package com.weather_service.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private String userID;
    private String username;
    private String location;
    private String preferredUnits;
    private String preferredLanguage;
    private boolean isPremium;
    private boolean isAdmin;
    private boolean alertsEnabled;
    private String apiKey;
    private int apiCallCount;
    private LocalDateTime lastApiCallTime;


    public User(){

    }

    public User(String userID, String username, String location, String preferredUnits, String preferredLanguage, boolean isPremium,boolean isAdmin, boolean alertsEnabled, String apiKey) {
        this.userID = userID;
        this.username = username;
        this.location = location;
        this.preferredUnits = preferredUnits;
        this.preferredLanguage = preferredLanguage;
        this.isPremium = isPremium;
        this.isAdmin = isAdmin;
        this.alertsEnabled = alertsEnabled;
        this.apiKey = apiKey;
        this.apiCallCount = 0;
        this.lastApiCallTime = null;
    }

    public void changeLanguage(String newLanguage) {
        this.preferredLanguage = newLanguage;
    }

    public void recordApiCall() {
        apiCallCount++;
        lastApiCallTime = LocalDateTime.now();
    }
}
