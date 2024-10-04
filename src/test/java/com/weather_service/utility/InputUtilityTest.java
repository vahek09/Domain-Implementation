package com.weather_service.utility;

import com.weather_service.domain.User;
import com.weather_service.service.core.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InputUtilityTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
    }

    @Test
    public void testGetValidUserId_Success() {
        // Simulate valid input (3 digits)
        String input = "123\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        String userId = InputUtility.getValidUserId(scanner);

        assertEquals("123", userId);
    }

    @Test
    public void testGetValidUserId_InvalidInput() {
        // Simulate invalid input followed by valid input
        String input = "12\nabc\n123\n";  // Invalid inputs followed by a valid one
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        String userId = InputUtility.getValidUserId(scanner);

        assertEquals("123", userId);
    }

    @Test
    public void testGetValidUsername_Success() {
        // Simulate valid input (letters only)
        String input = "validUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        String username = InputUtility.getValidUsername(scanner);

        assertEquals("validUser", username);
    }

    @Test
    public void testGetValidUsername_InvalidInput() {
        // Simulate invalid input followed by valid input
        String input = "user123\n!@#$\nvalidUser\n";  // Invalid inputs followed by a valid one
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        String username = InputUtility.getValidUsername(scanner);

        assertEquals("validUser", username);
    }

    @Test
    public void testGetValidDouble_Success() {
        // Simulate valid double input
        String input = "45.67\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        double value = InputUtility.getValidDouble(scanner, "Enter a number: ");

        assertEquals(45.67, value);
    }

    @Test
    public void testGetValidDouble_InvalidInput() {
        // Simulate invalid input followed by valid input
        String input = "abc\n12.34\n";  // Invalid input followed by valid input
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        double value = InputUtility.getValidDouble(scanner, "Enter a number: ");

        assertEquals(12.34, value);
    }

    @Test
    public void testGetCityName() {
        // Simulate city name input
        String input = "New York\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        String cityName = InputUtility.getCityName(scanner);

        assertEquals("New York", cityName);
    }

    @Test
    public void testFindUserById_Success() {
        String input = "1\n123\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        User mockUser = new User("123", "testUser", null, "metric", "en", false, null);
        when(userService.findUser("123")).thenReturn(mockUser);

        Scanner scanner = new Scanner(System.in);
        User user = InputUtility.findUserByIdOrUsername(scanner, userService);

        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        verify(userService, times(1)).findUser("123");
    }

    @Test
    public void testFindUserByUsername_Success() {
        String input = "2\ntestUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        User mockUser = new User("123", "testUser", null, "metric", "en", false, null);
        when(userService.findUserByUsername("testUser")).thenReturn(mockUser);

        Scanner scanner = new Scanner(System.in);
        User user = InputUtility.findUserByIdOrUsername(scanner, userService);

        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        verify(userService, times(1)).findUserByUsername("testUser");
    }

    @Test
    public void testFindUserById_InvalidChoice() {
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        User user = InputUtility.findUserByIdOrUsername(scanner, userService);

        assertNull(user);
        verify(userService, never()).findUser(anyString());
        verify(userService, never()).findUserByUsername(anyString());
    }

    @Test
    public void testFindUserById_UserNotFound() {
        String input = "1\n999\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        when(userService.findUser("999")).thenReturn(null);

        Scanner scanner = new Scanner(System.in);
        User user = InputUtility.findUserByIdOrUsername(scanner, userService);

        assertNull(user);
        verify(userService, times(1)).findUser("999");
    }
}
