package com.weather_service.service;

import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class APIKeyManagementServiceTest {

    private APIKeyManager apiKeyManager;
    private UserService userService;
    private APIKeyManagementService apiKeyManagementService;

    @BeforeEach
    public void setUp() {
        apiKeyManager = mock(APIKeyManager.class);
        userService = mock(UserService.class);
        apiKeyManagementService = new APIKeyManagementService(apiKeyManager, userService);
    }

    @Test
    public void testManageAPIKeys_AssignAPIKey() {
        String input = "001\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        User user = new User("001", "testUser", null, "metric", "en", false, null);
        when(userService.findUser("001")).thenReturn(user);

        apiKeyManagementService.manageAPIKeys(scanner);

        verify(apiKeyManager, times(1)).assignAPIKey(user);
        verify(apiKeyManager, never()).revokeAPIKey(any(User.class));
    }

    @Test
    public void testManageAPIKeys_RevokeAPIKey() {
        String input = "001\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        User user = new User("001", "testUser", null, "metric", "en", false, "apiKey123");
        when(userService.findUser("001")).thenReturn(user);

        apiKeyManagementService.manageAPIKeys(scanner);

        verify(apiKeyManager, times(1)).revokeAPIKey(user);
        verify(apiKeyManager, never()).assignAPIKey(any(User.class));
    }

    @Test
    public void testManageAPIKeys_UserNotFound() {
        String input = "999\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        when(userService.findUser("999")).thenReturn(null);

        apiKeyManagementService.manageAPIKeys(scanner);

        verify(apiKeyManager, never()).assignAPIKey(any(User.class));
        verify(apiKeyManager, never()).revokeAPIKey(any(User.class));
    }

    @Test
    public void testManageAPIKeys_InvalidChoice() {
        String input = "001\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        User user = new User("001", "testUser", null, "metric", "en", false, "apiKey123");
        when(userService.findUser("001")).thenReturn(user);

        apiKeyManagementService.manageAPIKeys(scanner);

        verify(apiKeyManager, never()).assignAPIKey(any(User.class));
        verify(apiKeyManager, never()).revokeAPIKey(any(User.class));
    }

    @Test
    public void testManageAPIKeys_AssignAPIKey_AlreadyAssigned() {
        String input = "001\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        User user = new User("001", "testUser", null, "metric", "en", false, "apiKey123");
        when(userService.findUser("001")).thenReturn(user);

        apiKeyManagementService.manageAPIKeys(scanner);

        verify(apiKeyManager, never()).assignAPIKey(user);
        verify(apiKeyManager, never()).revokeAPIKey(any(User.class));
    }

    @Test
    public void testManageAPIKeys_RevokeAPIKey_NoneAssigned() {
        String input = "001\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        User user = new User("001", "testUser", null, "metric", "en", false, null);
        when(userService.findUser("001")).thenReturn(user);

        apiKeyManagementService.manageAPIKeys(scanner);

        verify(apiKeyManager, never()).assignAPIKey(any(User.class));
        verify(apiKeyManager, never()).revokeAPIKey(user);
    }
}
