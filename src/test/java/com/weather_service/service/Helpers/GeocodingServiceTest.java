package com.weather_service.service.Helpers;

import com.weather_service.domain.Location;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GeocodingServiceTest {

    @Mock
    private WeatherApiClient weatherApiClient;

    private GeocodingService geocodingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        geocodingService = new GeocodingService(weatherApiClient, "testApiKey");
    }

    @Test
    public void testFetchCoordinatesForCity_Success() throws Exception {
        String cityName = "New York";
        String mockResponse = "[{\"name\":\"New York\",\"lat\":40.7128,\"lon\":-74.0060}]";

        when(weatherApiClient.makeApiCall(anyString())).thenReturn(mockResponse);

        Location location = geocodingService.fetchCoordinatesForCity(cityName);

        assertNotNull(location);
        assertEquals("New York", location.getCityName());
        assertEquals(40.7128, location.getLatitude());
        assertEquals(-74.0060, location.getLongitude());
    }

    @Test
    public void testFetchCoordinatesForCity_NoData() throws Exception {
        String cityName = "New York";
        String mockResponse = "[]";  // No data in the response (empty array)

        // Mock the API response to return an empty array
        when(weatherApiClient.makeApiCall(anyString())).thenReturn(mockResponse);

        // Test that JSONException is thrown when no location data is found
        JSONException exception = assertThrows(JSONException.class, () -> {
            geocodingService.fetchCoordinatesForCity(cityName);
        });

        // Assert that the exception message is what we expect
        assertEquals("No location data found in the Geocoding API response.", exception.getMessage());
    }



    @Test
    public void testFetchCoordinatesForCity_Failure() throws Exception {
        String cityName = "InvalidCity";

        when(weatherApiClient.makeApiCall(anyString())).thenThrow(new IOException("API call failed"));

        Location location = geocodingService.fetchCoordinatesForCity(cityName);

        assertNull(location);  // Expecting null in case of failure
    }

    @Test
    public void testFetchCityNameFromCoordinates_Success() throws Exception {
        double lat = 40.7128;
        double lon = -74.0060;
        String mockResponse = "[{\"name\":\"New York\"}]";

        when(weatherApiClient.makeApiCall(anyString())).thenReturn(mockResponse);

        String cityName = geocodingService.fetchCityNameFromCoordinates(lat, lon);

        assertEquals("New York", cityName);
    }

    @Test
    public void testFetchCityNameFromCoordinates_NoData() throws Exception {
        double lat = 40.7128;
        double lon = -74.0060;
        String mockResponse = "[]";

        when(weatherApiClient.makeApiCall(anyString())).thenReturn(mockResponse);

        assertThrows(JSONException.class, () -> {
            geocodingService.fetchCityNameFromCoordinates(lat, lon);
        });
    }

    @Test
    public void testFetchCityNameFromCoordinates_Failure() throws Exception {
        double lat = 0.0;
        double lon = 0.0;

        when(weatherApiClient.makeApiCall(anyString())).thenThrow(new IOException("API call failed"));

        String cityName = geocodingService.fetchCityNameFromCoordinates(lat, lon);

        assertNull(cityName);  // Expecting null in case of failure
    }
}
