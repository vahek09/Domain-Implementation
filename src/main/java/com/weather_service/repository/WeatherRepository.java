package com.weather_service.repository;

import com.weather_service.domain.WeatherData;
import java.util.HashMap;
import java.util.Map;

public class WeatherRepository {
    private final Map<String, WeatherData> weatherDataMap = new HashMap<>();

    public void saveWeatherData(String locationKey, WeatherData weatherData) {
        weatherDataMap.put(locationKey, weatherData);
    }

    public WeatherData getWeatherData(String locationKey) {
        return weatherDataMap.get(locationKey);
    }

    public boolean hasWeatherData(String locationKey) {
        return weatherDataMap.containsKey(locationKey);
    }

    public void clearAllWeatherData() {
        weatherDataMap.clear();
    }
}
