package com.weather_service.service.core;

import com.weather_service.domain.Location;
import com.weather_service.domain.User;
import com.weather_service.domain.WeatherData;
import com.weather_service.repository.WeatherRepository;
import com.weather_service.service.Helpers.GeocodingService;
import com.weather_service.service.Helpers.WeatherApiClient;
import com.weather_service.service.Helpers.WeatherDataParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {

    private WeatherRepository weatherRepository;
    private WeatherApiClient weatherApiClient;
    private WeatherDataParser weatherDataParser;
    private GeocodingService geocodingService;
    private WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        weatherRepository = mock(WeatherRepository.class);
        weatherApiClient = mock(WeatherApiClient.class);
        weatherDataParser = mock(WeatherDataParser.class);
        geocodingService = mock(GeocodingService.class);
        weatherService = new WeatherService(weatherRepository, weatherApiClient, weatherDataParser, geocodingService, "test-api-key");
    }

    @Test
    public void testFetchCurrentWeather_weatherInCache() throws Exception {
        // Arrange
        Location location = new Location(40.7128, -74.0060, "New York");
        User user = new User("001", "testUser", location, "metric", "en", false, null);
        WeatherData cachedWeather = new WeatherData();
        when(weatherRepository.hasWeatherData(anyString())).thenReturn(true);
        when(weatherRepository.getWeatherData(anyString())).thenReturn(cachedWeather);

        // Act
        WeatherData result = weatherService.fetchCurrentWeather(user);

        // Assert
        assertEquals(cachedWeather, result);
        verify(weatherRepository, times(1)).getWeatherData(anyString());
        verify(weatherApiClient, never()).makeApiCall(anyString());
    }

    @Test
    public void testFetchCurrentWeather_weatherNotInCache() throws Exception {
        // Arrange
        Location location = new Location(40.7128, -74.0060, "New York");
        User user = new User("001", "testUser", location, "metric", "en", false, null);
        String jsonResponse = "{}";  // mock response
        WeatherData parsedWeather = new WeatherData();

        when(weatherRepository.hasWeatherData(anyString())).thenReturn(false);
        when(weatherApiClient.makeApiCall(anyString())).thenReturn(jsonResponse);
        when(weatherDataParser.parseWeatherData(jsonResponse)).thenReturn(parsedWeather);

        // Act
        WeatherData result = weatherService.fetchCurrentWeather(user);

        // Assert
        assertEquals(parsedWeather, result);
        verify(weatherRepository, times(1)).saveWeatherData(anyString(), any(WeatherData.class));
    }

    // Test case for missing location or city name
    @Test
    public void testFetchCurrentWeather_missingLocationOrCityName() {
        // Arrange
        User userWithoutLocation = new User("001", "testUser", null, "metric", "en", false, null);
        User userWithEmptyCityName = new User("002", "testUser", new Location(0, 0, ""), "metric", "en", false, null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> weatherService.fetchCurrentWeather(userWithoutLocation),
                "Expected exception for missing location");

        assertThrows(IllegalArgumentException.class, () -> weatherService.fetchCurrentWeather(userWithEmptyCityName),
                "Expected exception for empty city name");
    }

    // Test case for missing latitude or longitude (triggering geocoding)
    @Test
    public void testFetchCurrentWeather_geocodingTriggered() throws Exception {
        // Arrange
        Location locationWithoutCoordinates = new Location(0, 0, "New York");
        User user = new User("001", "testUser", locationWithoutCoordinates, "metric", "en", false, null);
        Location geocodedLocation = new Location(40.7128, -74.0060, "New York");

        String jsonResponse = "{}";  // mock response
        WeatherData parsedWeather = new WeatherData();

        when(weatherRepository.hasWeatherData(anyString())).thenReturn(false);
        when(geocodingService.fetchCoordinatesForCity(anyString())).thenReturn(geocodedLocation);
        when(weatherApiClient.makeApiCall(anyString())).thenReturn(jsonResponse);
        when(weatherDataParser.parseWeatherData(jsonResponse)).thenReturn(parsedWeather);

        // Act
        WeatherData result = weatherService.fetchCurrentWeather(user);

        // Assert
        assertEquals(parsedWeather, result);
        verify(geocodingService, times(1)).fetchCoordinatesForCity(anyString());
        verify(weatherRepository, times(1)).saveWeatherData(anyString(), any(WeatherData.class));
        assertEquals(geocodedLocation, user.getLocation()); // Ensure location is updated in the user object
    }

    @Test
    public void testFetchCurrentWeather_ApiCallFails() throws Exception {
        // Arrange
        Location location = new Location(40.7128, -74.0060, "New York");
        User user = new User("001", "testUser", location, "metric", "en", false, null);

        when(weatherRepository.hasWeatherData(anyString())).thenReturn(false);
        when(weatherApiClient.makeApiCall(anyString())).thenThrow(new RuntimeException("API call failed"));

        // Act
        WeatherData result = weatherService.fetchCurrentWeather(user);

        // Assert: The method should return null and print the error
        assertNull(result);
        verify(weatherApiClient, times(1)).makeApiCall(anyString());
    }

    // Test for fetchFiveDayForecast
    @Test
    public void testFetchFiveDayForecast_Success() throws Exception {
        // Arrange
        Location location = new Location(40.7128, -74.0060, "New York");
        User user = mock(User.class);  // Mock the user object
        when(user.getLocation()).thenReturn(location);
        when(user.getPreferredUnits()).thenReturn("metric");
        when(user.getPreferredLanguage()).thenReturn("en");

        String jsonResponse = "{}";  // Mocked response
        List<WeatherData> forecastData = List.of(new WeatherData(), new WeatherData());  // Mocked parsed data

        // Mocking the API call and the parser
        when(weatherApiClient.makeApiCall(anyString())).thenReturn(jsonResponse);
        when(weatherDataParser.parseForecastData(jsonResponse)).thenReturn(forecastData);

        // Act
        List<WeatherData> result = weatherService.fetchFiveDayForecast(user);

        // Assert
        assertEquals(forecastData, result);  // Ensure the returned list is correct
        verify(weatherApiClient, times(1)).makeApiCall(anyString());
        verify(weatherDataParser, times(1)).parseForecastData(jsonResponse);
        verify(user, times(1)).recordApiCall();  // Ensure the API call count is recorded
    }

    @Test
    public void testFetchFiveDayForecast_Exception() throws Exception {
        // Arrange
        Location location = new Location(40.7128, -74.0060, "New York");
        User user = mock(User.class);  // Mock the user object
        when(user.getLocation()).thenReturn(location);
        when(user.getPreferredUnits()).thenReturn("metric");
        when(user.getPreferredLanguage()).thenReturn("en");

        when(weatherApiClient.makeApiCall(anyString())).thenThrow(new RuntimeException("API call failed"));

        // Act
        List<WeatherData> result = weatherService.fetchFiveDayForecast(user);

        // Assert
        assertNull(result);  // Ensure null is returned in case of an exception
        verify(weatherApiClient, times(1)).makeApiCall(anyString());
        verify(user, never()).recordApiCall();  // Ensure no API call count is recorded if there's an error
    }

}
