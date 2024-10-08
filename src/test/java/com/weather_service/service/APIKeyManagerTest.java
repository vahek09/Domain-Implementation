package com.weather_service.service;

import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class APIKeyManagerTest {

    private static final String API_KEY_ENV = "OPENWEATHER_API_KEY";
    private APIKeyManager apiKeyManager;
    private UserService userService;
    String sharedApiKey;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        apiKeyManager = new APIKeyManager(userService);
        Dotenv dotenv = Dotenv.load();
        sharedApiKey = dotenv.get(API_KEY_ENV);
    }

    @Test
    public void testAssignAPIKey_Success() {
        // Arrange
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("testUser");

        // Act
        apiKeyManager.assignAPIKey(user);

        // Assert
        verify(user, times(1)).setApiKey(sharedApiKey);
        verify(userService, times(1)).updateUser(user);
        System.out.println("API key assigned successfully.");
    }

    @Test
    public void testAssignAPIKey_NullUser() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> apiKeyManager.assignAPIKey(null),
                "Expected exception for null user.");
    }

    @Test
    public void testRevokeAPIKey_Success() {
        // Arrange
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("testUser");

        // Act
        apiKeyManager.revokeAPIKey(user);

        // Assert
        verify(user, times(1)).setApiKey(null);
        verify(userService, times(1)).updateUser(user);  // Ensure userService's updateUser method is called
        System.out.println("API key revoked successfully.");
    }

    @Test
    public void testRevokeAPIKey_NullUser() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> apiKeyManager.revokeAPIKey(null),
                "Expected exception for null user.");
    }
}
