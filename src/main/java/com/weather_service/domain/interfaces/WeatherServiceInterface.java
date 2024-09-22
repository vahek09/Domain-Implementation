package com.weather_service.domain.interfaces;

import com.weather_service.domain.*;

import java.util.List;

public interface WeatherServiceInterface {
    WeatherData fetchCurrentWeather(Location location);

    WeatherData fetchHistoricalWeather(Location location, String date);

    List<WeatherData> fetch7DayForecast(Location location);

    List<WeatherData> fetchHourlyForecast(Location location);

    List<WeatherAlert> fetchWeatherAlerts(Location location);

    void setUnits(String units);

    void setLanguage(String language);

    UVIndex getUVIndex(Location location);

    boolean isPremium(User user);
}
