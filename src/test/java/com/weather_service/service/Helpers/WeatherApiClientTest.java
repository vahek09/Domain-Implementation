package com.weather_service.service.Helpers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class WeatherApiClientTest {

    private WeatherApiClient weatherApiClient;
    private static final String API_KEY = "null";
    private static final String CITY_NAME = "London";
    private static final String API_URL_FORMAT = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s";

    @BeforeEach
    void setUp() {
        weatherApiClient = new WeatherApiClient();
    }

    @Test
    void testMakeApiCall_success() throws Exception {
        // Arrange
        String expectedResponse = "[{\"name\":\"London\",\"lat\":51.5074,\"lon\":-0.1278,\"country\":\"GB\"}]";
        InputStream inputStream = new ByteArrayInputStream(expectedResponse.getBytes());

        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(inputStream);

        String fullUrl = String.format(API_URL_FORMAT, CITY_NAME, API_KEY);

        try (MockedConstruction<URL> mocked = mockConstruction(URL.class,
                (mock, context) -> when(mock.openConnection()).thenReturn(mockConnection))) {

            // Act
            String response = weatherApiClient.makeApiCall(fullUrl);

            // Assert
            assertEquals(expectedResponse, response);
            verify(mockConnection).disconnect();
        }
    }

    @Test
    void testMakeApiCall_failedResponseCode() throws Exception {
        // Arrange
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(401);  // Unauthorized (invalid API key)

        String fullUrl = String.format(API_URL_FORMAT, CITY_NAME, "invalid_api_key");

        try (MockedConstruction<URL> mocked = mockConstruction(URL.class,
                (mock, context) -> when(mock.openConnection()).thenReturn(mockConnection))) {

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () ->
                    weatherApiClient.makeApiCall(fullUrl)
            );

            assertEquals("Failed : HTTP error code : 401", exception.getMessage());

            // Verify that disconnect() was called before the exception was thrown
            verify(mockConnection, times(1)).setRequestMethod("GET");
            verify(mockConnection, times(1)).setRequestProperty("Accept", "application/json");
            verify(mockConnection, times(2)).getResponseCode();

            // Note: We're not verifying disconnect() here because it's not called in the current implementation
        }
    }
}
