package com.weather_service.service;

import com.weather_service.domain.SystemSettings;
import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SettingsManagerTest {

    private UserService userService;
    private SettingsManager settingsManager;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);  // Mock UserService
        settingsManager = new SettingsManager(userService);  // Initialize SettingsManager with the mocked UserService
    }

    @Test
    public void testGetSystemSettings() {
        // Arrange
        User user = mock(User.class);
        when(user.getPreferredLanguage()).thenReturn("en");
        when(user.getPreferredUnits()).thenReturn("metric");

        // Act
        SystemSettings systemSettings = settingsManager.getSystemSettings(user);

        // Assert
        assertNotNull(systemSettings);
        assertEquals("en", systemSettings.getPreferredLanguage());
        assertEquals("metric", systemSettings.getPreferredUnits());
    }

    @Test
    public void testUpdateSettings_Success() {
        // Arrange
        User user = mock(User.class);

        // Act
        settingsManager.updateSettings(user, "fr", "imperial");

        // Assert
        verify(user, times(1)).setPreferredLanguage("fr");
        verify(user, times(1)).setPreferredUnits("imperial");
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    public void testConstructor_NullUserService() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new SettingsManager(null),
                "Expected exception for null UserService.");
    }
}
