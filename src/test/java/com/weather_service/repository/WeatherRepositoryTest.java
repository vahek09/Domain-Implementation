package com.weather_service.repository;

import com.weather_service.domain.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherRepositoryTest {

    private WeatherRepository weatherRepository;

    @BeforeEach
    public void setUp() {
        weatherRepository = new WeatherRepository();
    }

    @Test
    public void testSaveAndRetrieveWeatherData() {
        // Arrange
        String locationKey = "NewYork";
        WeatherData weatherData = new WeatherData();
        weatherData.setCityName("New York");
        weatherData.setTemperature(20.5);

        // Act
        weatherRepository.saveWeatherData(locationKey, weatherData);
        WeatherData retrievedData = weatherRepository.getWeatherData(locationKey);

        // Assert
        assertNotNull(retrievedData);
        assertEquals("New York", retrievedData.getCityName());
        assertEquals(20.5, retrievedData.getTemperature());
    }

    @Test
    public void testHasWeatherData_True() {
        // Arrange
        String locationKey = "NewYork";
        WeatherData weatherData = new WeatherData();
        weatherRepository.saveWeatherData(locationKey, weatherData);

        // Act
        boolean result = weatherRepository.hasWeatherData(locationKey);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasWeatherData_False() {
        // Arrange
        String locationKey = "NonExistentCity";

        // Act
        boolean result = weatherRepository.hasWeatherData(locationKey);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testClearAllWeatherData() {
        // Arrange
        String locationKey = "NewYork";
        WeatherData weatherData = new WeatherData();
        weatherRepository.saveWeatherData(locationKey, weatherData);

        // Act
        weatherRepository.clearAllWeatherData();
        boolean result = weatherRepository.hasWeatherData(locationKey);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetWeatherData_NotFound() {
        // Act
        WeatherData result = weatherRepository.getWeatherData("NonExistentCity");

        // Assert
        assertNull(result);
    }
}
