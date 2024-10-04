package com.weather_service.service.core;

import com.weather_service.domain.Location;
import com.weather_service.domain.User;
import com.weather_service.domain.WeatherData;
import com.weather_service.repository.WeatherRepository;
import com.weather_service.service.Helpers.GeocodingService;
import com.weather_service.service.Helpers.WeatherApiClient;
import com.weather_service.service.Helpers.WeatherDataParser;

import java.util.List;


public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherApiClient weatherApiClient;
    private final WeatherDataParser weatherDataParser;
    private final GeocodingService geocodingService;
    private static String apiKey ;

    public WeatherService(WeatherRepository weatherRepository, WeatherApiClient weatherApiClient, WeatherDataParser weatherDataParser, GeocodingService geocodingService, String apiKey) {
        this.weatherRepository = weatherRepository;
        this.weatherApiClient = weatherApiClient;
        this.weatherDataParser = weatherDataParser;
        this.geocodingService = geocodingService;
        WeatherService.apiKey = apiKey;
    }

    public WeatherData fetchCurrentWeather(User user) throws Exception {
        Location location = user.getLocation();

        if (location == null || location.getCityName() == null || location.getCityName().isEmpty()) {
            throw new IllegalArgumentException("User location or city name is missing.");
        }

        if (location.getLatitude() == 0 || location.getLongitude() == 0) {
            location = geocodingService.fetchCoordinatesForCity(location.getCityName());
            user.setLocation(location);
        }

        String locationKey = generateLocationKey(location);

        if (weatherRepository.hasWeatherData(locationKey)) {
            return weatherRepository.getWeatherData(locationKey);
        }

        String apiUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%.2f&lon=%.2f&appid=%s&units=%s&lang=%s",
                location.getLatitude(), location.getLongitude(), apiKey, user.getPreferredUnits(), user.getPreferredLanguage());

        try {
            String jsonResponse = weatherApiClient.makeApiCall(apiUrl);
            WeatherData weatherData = weatherDataParser.parseWeatherData(jsonResponse);
            weatherRepository.saveWeatherData(locationKey, weatherData);

            user.recordApiCall();

            return weatherData;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public List<WeatherData> fetchFiveDayForecast(User user) {
        Location location = user.getLocation();
        String apiUrl = String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%.2f&lon=%.2f&appid=%s&units=%s&lang=%s",
                location.getLatitude(), location.getLongitude(), apiKey, user.getPreferredUnits(), user.getPreferredLanguage());

        try {
            String jsonResponse = weatherApiClient.makeApiCall(apiUrl);

            user.recordApiCall();

            return weatherDataParser.parseForecastData(jsonResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String generateLocationKey(Location location) {
        return String.format("%.2f,%.2f", location.getLatitude(), location.getLongitude());
    }
}
