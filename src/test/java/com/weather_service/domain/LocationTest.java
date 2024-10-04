package com.weather_service.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {

    private Location location;

    @BeforeEach
    public void setUp() {
        location = new Location();
    }

    @Test
    public void testSetCoordinates() {
        // Act
        location.setCoordinates(40.7128, -74.0060);

        // Assert
        assertEquals(40.7128, location.getLatitude(), 0.0001);
        assertEquals(-74.0060, location.getLongitude(), 0.0001);
    }

    @Test
    public void testGetLocationData() {
        // Arrange
        location.setCityName("New York");
        location.setCoordinates(40.7128, -74.0060);

        // Act
        String locationData = location.getLocationData();

        // Assert
        assertEquals("Location: New York (Lat: 40.7128, Lon: -74.006)", locationData);
    }

    @Test
    public void testConstructorWithArgs() {
        // Arrange & Act
        Location customLocation = new Location(51.5074, -0.1278, "London");

        // Assert
        assertEquals(51.5074, customLocation.getLatitude(), 0.0001);
        assertEquals(-0.1278, customLocation.getLongitude(), 0.0001);
        assertEquals("London", customLocation.getCityName());
    }

    @Test
    public void testSetAndGetCityName() {
        // Act
        location.setCityName("Paris");

        // Assert
        assertEquals("Paris", location.getCityName());
    }
}
