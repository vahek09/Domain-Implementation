package com.weather_service.service;

import com.weather_service.domain.*;
import com.weather_service.repository.UserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WeatherService {
//    private String baseURL = "https://api.openweathermap.org/data/3.0/onecall";
//    private User currentUser; // Current user context
//    private UserRepository userRepository;
//
//    public WeatherService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    // Set the current user
//    public void setCurrentUser(String userId) {
//        this.currentUser = userRepository.findById(userId);
//    }
//
//    private String buildUrl(Location location, String exclude) {
//        if (currentUser == null) {
//            throw new IllegalStateException("Current user is not set.");
//        }
//        String apiKey = currentUser.getApiKey(); // Get API key from current user
//        return String.format("%s?lat=%f&lon=%f&exclude=%s&appid=%s&units=%s&lang=%s",
//                baseURL, location.getLatitude(), location.getLongitude(), exclude, apiKey, currentUser.getPreferredUnits(), currentUser.getPreferredLanguage());
//    }
//
//    public WeatherData fetchCurrentWeather(Location location) {
//        try {
//            String url = buildUrl(location, "minutely,hourly,daily,alerts");
//            return makeApiCall(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public WeatherData fetchHistoricalWeather(Location location, String date) {
//        return null;
//    }
//
//    public List<Forecast> fetch7DayForecast(Location location) {
//        try {
//            String url = buildUrl(location, "current,hourly,alerts");
//            return makeForecastApiCall(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public List<WeatherAlert> fetchWeatherAlerts(Location location) {
//        try {
//            String url = buildUrl(location, "current,minutely,hourly,daily");
//            return makeWeatherAlertApiCall(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public UVIndex getUVIndex(Location location) {
//        try {
//            String url = buildUrl(location, "current,minutely,hourly,daily");
//            return makeUVIndexApiCall(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private WeatherData makeApiCall(String urlString) {
//        // Implement API call logic
//        return null; // Replace with actual implementation
//    }
//
//    private List<Forecast> makeForecastApiCall(String urlString) {
//        // Implement logic to call API and return Forecast list
//        return null; // Replace with actual implementation
//    }
//
//    private List<WeatherAlert> makeWeatherAlertApiCall(String urlString) {
//        // Implement logic to call API and return WeatherAlert list
//        return null; // Replace with actual implementation
//    }
//
//    private UVIndex makeUVIndexApiCall(String urlString) {
//        // Implement logic to call API and return UVIndex
//        return null; // Replace with actual implementation
//    }
}
