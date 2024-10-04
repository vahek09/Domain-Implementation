package com.weather_service.service;

import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class APIKeyManagerTest {

    private APIKeyManager apiKeyManager;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        apiKeyManager = new APIKeyManager(userService);
    }

    @Test
    public void testAssignAPIKey_Success() {
        // Arrange
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("testUser");

        // Act
        apiKeyManager.assignAPIKey(user);

        // Assert
        verify(user, times(1)).setApiKey("null");
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
