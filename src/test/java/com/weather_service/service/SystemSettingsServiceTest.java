package com.weather_service.service;

import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class SystemSettingsServiceTest {

    private UserService userService;
    private SettingsManager settingsManager;
    private SystemSettingsService systemSettingsService;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        settingsManager = mock(SettingsManager.class);
        systemSettingsService = new SystemSettingsService(settingsManager, userService);
    }

    @Test
    public void testManageSystemSettings_UserFound() {
        // Arrange
        String input = "001\nEnglish\nmetric\n";  // Simulate user input for ID, language, and units
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        User user = new User("001", "testUser", null, "metric", "en", false, null);
        when(userService.findUser("001")).thenReturn(user);

        // Act
        systemSettingsService.manageSystemSettings(scanner);

        // Assert
        verify(settingsManager, times(1)).updateSettings(user, "English", "metric");
        verify(userService, times(1)).findUser("001");
    }

    @Test
    public void testManageSystemSettings_UserNotFound() {
        // Arrange
        String input = "001\nEnglish\nmetric\n";  // Simulate user input for ID, language, and units
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        when(userService.findUser("001")).thenReturn(null);  // User not found

        // Act
        systemSettingsService.manageSystemSettings(scanner);

        // Assert
        verify(settingsManager, never()).updateSettings(any(), anyString(), anyString());
        verify(userService, times(1)).findUser("001");
    }
}
