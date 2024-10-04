package com.weather_service.service;

import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsageStatisticsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsageStatisticsService usageStatisticsService;

    private StringBuilder output;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        usageStatisticsService = new UsageStatisticsService(userService);
        output = new StringBuilder();
    }

    @Test
    public void testViewUsageStatistics_NoUsers() {
        // Simulate no users in the system
        when(userService.listAllUsers()).thenReturn(Collections.emptyList());

        output.append("User Usage Statistics:\n");
        output.append("-----------------------------\n");

        usageStatisticsService.viewUsageStatistics();

        verify(userService, times(1)).listAllUsers();

        String expectedOutput = "User Usage Statistics:\n-----------------------------\n";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testViewUsageStatistics_WithUsers() {
        // Arrange - mock two users
        User user1 = new User("001", "user1", null, "metric", "en", false, "apikey1");
        user1.setApiCallCount(10);
        user1.setLastApiCallTime(LocalDateTime.of(2023, 9, 23, 14, 0));

        User user2 = new User("002", "user2", null, "imperial", "en", false, "apikey2");
        user2.setApiCallCount(20);
        user2.setLastApiCallTime(LocalDateTime.of(2023, 9, 23, 15, 0));

        when(userService.listAllUsers()).thenReturn(Arrays.asList(user1, user2));

        output.append("User Usage Statistics:\n");
        output.append("Username: user1\n");
        output.append("API Key: apikey1\n");
        output.append("Preferred Units: metric\n");
        output.append("API Call Count: 10\n");
        output.append("Last API Call: 2023-09-23T14:00\n");
        output.append("-----------------------------\n");
        output.append("Username: user2\n");
        output.append("API Key: apikey2\n");
        output.append("Preferred Units: imperial\n");
        output.append("API Call Count: 20\n");
        output.append("Last API Call: 2023-09-23T15:00\n");
        output.append("-----------------------------\n");

        usageStatisticsService.viewUsageStatistics();

        verify(userService, times(1)).listAllUsers();

        // Assert expected output
        String expectedOutput = "User Usage Statistics:\n" +
                "Username: user1\n" +
                "API Key: apikey1\n" +
                "Preferred Units: metric\n" +
                "API Call Count: 10\n" +
                "Last API Call: 2023-09-23T14:00\n" +
                "-----------------------------\n" +
                "Username: user2\n" +
                "API Key: apikey2\n" +
                "Preferred Units: imperial\n" +
                "API Call Count: 20\n" +
                "Last API Call: 2023-09-23T15:00\n" +
                "-----------------------------\n";

        assertEquals(expectedOutput, output.toString());
    }
}
