package com.weather_service.service.core;

import com.weather_service.domain.*;
import com.weather_service.service.Helpers.GeocodingService;
import com.weather_service.service.*;
import com.weather_service.utility.InputUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AppServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private WeatherService weatherService;
    @Mock
    private AdminService adminService;
    @Mock
    private GeocodingService geocodingService;
    @Mock
    private SettingsManager settingsManager;

    @InjectMocks
    private AppService appService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    @Test
    public void testRunApp_ExitOption() throws Exception {
        // Given
        String input = "8\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // When
        appService.runApp();

        // Then
        // No assertions as we just verify that the app exited without errors
    }

    @Test
    public void testAdminLogin_Success() {
        // Given
        String input = "AdminUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        User adminUser = new User("admin1", "AdminUser", null, "metric", "en", true, "apiKey");
        when(userService.findUserByUsername("AdminUser")).thenReturn(adminUser);

        // When
        boolean result = appService.adminLogin(new Scanner(System.in));

        // Then
        assertTrue(result);
    }

    @Test
    public void testAdminLogin_Failure() {
        // Given
        String input = "NonAdminUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        when(userService.findUserByUsername("NonAdminUser")).thenReturn(null);

        // When
        boolean result = appService.adminLogin(new Scanner(System.in));

        // Then
        assertFalse(result);
    }

    @Test
    public void testFetchCityNameFromCoordinates_Success() throws Exception {
        // Given
        String input = "40.7128\n-74.0060\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        when(geocodingService.fetchCityNameFromCoordinates(40.7128, -74.0060)).thenReturn("New York");

        // When
        appService.fetchCityNameFromCoordinates(new Scanner(System.in));

        // Then
        verify(geocodingService, times(1)).fetchCityNameFromCoordinates(40.7128, -74.0060);
    }

    @Test
    public void testCheckSystemSettings_Success() {
        // Given
        String input = "001\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        User user = new User("001", "testUser", null, "metric", "en", false, "apiKey");
        SystemSettings settings = new SystemSettings("en", "metric");
        when(userService.findUser("001")).thenReturn(user);
        when(settingsManager.getSystemSettings(user)).thenReturn(settings);

        // When
        appService.checkSystemSettings(new Scanner(System.in));

        // Then
        verify(settingsManager, times(1)).getSystemSettings(user);
    }

    @Test
    public void testCheckSystemSettings_UserNotFound() {
        // Given
        String input = "NonExistingUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        when(userService.findUser(anyString())).thenReturn(null);

        // When
        appService.checkSystemSettings(new Scanner(System.in));

        // Then
        verify(settingsManager, never()).getSystemSettings(any(User.class));
    }

    @Test
    public void testFetchCoordinatesForCity_Success() throws Exception {
        // Given
        String input = "New York\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Location mockLocation = new Location(40.7128, -74.0060, "New York");
        when(geocodingService.fetchCoordinatesForCity("New York")).thenReturn(mockLocation);

        // When
        appService.fetchCoordinatesForCity(new Scanner(System.in));

        // Then
        verify(geocodingService, times(1)).fetchCoordinatesForCity("New York");
    }

    @Test
    public void testFetchCoordinatesForCity_Failure() throws Exception {
        // Given
        String input = "UnknownCity\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        when(geocodingService.fetchCoordinatesForCity("UnknownCity")).thenReturn(null);

        // When
        appService.fetchCoordinatesForCity(new Scanner(System.in));

        // Then
        verify(geocodingService, times(1)).fetchCoordinatesForCity("UnknownCity");
    }

    @Test
    public void testFetchCurrentWeather_UserNotFound() throws Exception {
        // Given
        try (MockedStatic<InputUtility> mockedInputUtility = mockStatic(InputUtility.class)) {
            mockedInputUtility.when(() -> InputUtility.findUserByIdOrUsername(any(Scanner.class), any(UserService.class)))
                    .thenReturn(null);

            // When
            appService.fetchCurrentWeather(new Scanner(System.in));

            // Then
            verify(weatherService, never()).fetchCurrentWeather(any(User.class));
        }
    }

    @Test
    public void testFetchCurrentWeather_Success() throws Exception {
        // Given
        try (MockedStatic<InputUtility> mockedInputUtility = mockStatic(InputUtility.class)) {
            User mockUser = new User("001", "testUser", null, "metric", "en", false, "apiKey");
            WeatherData mockWeatherData = new WeatherData();
            mockWeatherData.setCityName("New York");
            mockWeatherData.setTemperature(25.0);
            mockWeatherData.setWeatherDescription("Sunny");

            mockedInputUtility.when(() -> InputUtility.findUserByIdOrUsername(any(Scanner.class), any(UserService.class)))
                    .thenReturn(mockUser);
            when(weatherService.fetchCurrentWeather(mockUser)).thenReturn(mockWeatherData);

            // When
            appService.fetchCurrentWeather(new Scanner(System.in));

            // Then
            verify(weatherService, times(1)).fetchCurrentWeather(mockUser);
        }
    }

    @Test
    public void testFetchCurrentWeather_WeatherDataNotFound() throws Exception {
        // Given
        try (MockedStatic<InputUtility> mockedInputUtility = mockStatic(InputUtility.class)) {
            User mockUser = new User("001", "testUser", null, "metric", "en", false, "apiKey");

            mockedInputUtility.when(() -> InputUtility.findUserByIdOrUsername(any(Scanner.class), any(UserService.class)))
                    .thenReturn(mockUser);
            when(weatherService.fetchCurrentWeather(mockUser)).thenReturn(null);

            // When
            appService.fetchCurrentWeather(new Scanner(System.in));

            // Then
            verify(weatherService, times(1)).fetchCurrentWeather(mockUser);
        }
    }

    @Test
    public void testFetchFiveDayForecast_UserNotFound() {
        // Arrange
        String userId = "nonexistent";
        when(userService.findUser(userId)).thenReturn(null);

        // Simulate user input
        System.setIn(new ByteArrayInputStream(userId.getBytes()));
        Scanner scanner = new Scanner(System.in);

        // Act
        appService.fetchFiveDayForecast(scanner);

        // Assert
        verify(userService).findUser(userId);
        verify(weatherService, never()).fetchFiveDayForecast(any(User.class));
        assertTrue(outContent.toString().contains("User not found."));
    }

    @Test
    public void testFetchFiveDayForecast_EmptyForecastData() {
        // Arrange
        String userId = "user123";
        User mockUser = new User(userId, "testUser", new Location(40.7128, -74.0060,"New York"), "metric", "en", false, "apiKey");

        when(userService.findUser(userId)).thenReturn(mockUser);
        when(weatherService.fetchFiveDayForecast(mockUser)).thenReturn(Collections.emptyList());

        // Simulate user input
        System.setIn(new ByteArrayInputStream(userId.getBytes()));
        Scanner scanner = new Scanner(System.in);

        // Act
        appService.fetchFiveDayForecast(scanner);

        // Assert
        verify(userService).findUser(userId);
        verify(weatherService).fetchFiveDayForecast(mockUser);
        assertTrue(outContent.toString().contains("Failed to fetch forecast data."));
    }

    @Test
    public void testUpdateUserLocation_UserNotFound() {
        // Given
        String input = "testUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        try (MockedStatic<InputUtility> mockedInputUtility = mockStatic(InputUtility.class)) {
            mockedInputUtility.when(() -> InputUtility.findUserByIdOrUsername(any(Scanner.class), eq(userService)))
                    .thenReturn(null);

            // When
            appService.updateUserLocation(new Scanner(System.in));

            // Then
            assertTrue(outContent.toString().contains("User not found."));
            verify(userService, never()).updateUser(any(User.class));
        }
    }

    @Test
    public void testUpdateUserLocation_Success() {
        // Given
        User mockUser = new User("001", "testUser", null, "metric", "en", false, "apiKey");

        String input = "New York\n40.7128\n-74.0060\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        try (MockedStatic<InputUtility> mockedInputUtility = mockStatic(InputUtility.class)) {
            mockedInputUtility.when(() -> InputUtility.findUserByIdOrUsername(any(Scanner.class), eq(userService)))
                    .thenReturn(mockUser);
            mockedInputUtility.when(() -> InputUtility.getValidDouble(any(Scanner.class), anyString()))
                    .thenReturn(40.7128, -74.0060);

            // When
            appService.updateUserLocation(new Scanner(System.in));

            // Then
            verify(userService, times(1)).updateUser(mockUser);
            assertEquals("New York", mockUser.getLocation().getCityName());
            assertEquals(40.7128, mockUser.getLocation().getLatitude(), 0.0001);
            assertEquals(-74.0060, mockUser.getLocation().getLongitude(), 0.0001);
            assertTrue(outContent.toString().contains("User location updated to: New York"));
        }
    }



}
