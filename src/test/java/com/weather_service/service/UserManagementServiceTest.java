package com.weather_service.service;

import com.weather_service.domain.Location;
import com.weather_service.domain.User;
import com.weather_service.domain.Admin;
import com.weather_service.service.core.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class UserManagementServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserManagementService userManagementService;

    private Admin loggedInAdmin;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userManagementService = new UserManagementService(userService);

        Location adminLocation = new Location(40.7128, -74.0060, "New York");
        loggedInAdmin = new Admin("admin1", "AdminUser", adminLocation, "metric", "en", true, null);
    }

    @Test
    public void testCreateUser_Success() {
        // Simulate user input for creating a new user
        String input = "123\nvalidUser\nNew York\n40.7128\n-74.0060\ntrue\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        // Call the createUser method
        userManagementService.createUser(scanner);

        // Verify
        verify(userService, times(1)).createUser(
                eq("123"),
                eq("validUser"),
                any(Location.class),
                eq(true)
        );
    }

    @Test
    public void testCreateUser_InvalidInput() {
        String input = "123\nvalidUser\nNew York\nabc\n40.7128\n-xyz\n-74.0060\ntrue\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        userManagementService.createUser(scanner);

        verify(userService, times(1)).createUser(
                eq("123"),
                eq("validUser"),
                any(Location.class),
                eq(true)
        );
    }

    @Test
    public void testDeleteUser_Success() {
        User mockUser = new User("001", "testUser", null, "metric", "en", false, null);
        when(userService.findUserByUsername("testUser")).thenReturn(mockUser);

        String input = "2\ntestUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        userManagementService.deleteUser(scanner, loggedInAdmin);

        verify(userService, times(1)).deleteUser("001");
        System.out.println("User deleted: testUser");
    }

    @Test
    public void testDeleteUser_NotFound() {
        when(userService.findUserByUsername("nonexistentUser")).thenReturn(null);

        String input = "2\nnonexistentUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        userManagementService.deleteUser(scanner, loggedInAdmin);

        verify(userService, never()).deleteUser(anyString());
        System.out.println("User not found.");
    }

    @Test
    public void testDeleteUser_LoggedInAdminCannotBeDeleted() {
        when(userService.findUserByUsername("AdminUser")).thenReturn(loggedInAdmin);

        String input = "2\nAdminUser\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        userManagementService.deleteUser(scanner, loggedInAdmin);

        verify(userService, never()).deleteUser(anyString());
        System.out.println("You cannot delete the admin user you are currently logged in with.");
    }

    @Test
    public void testViewAllUsers_NoUsers() {
        when(userService.listAllUsers()).thenReturn(Collections.emptyList());

        userManagementService.viewAllUsers();

        System.out.println("No users found.");
    }

    @Test
    public void testViewAllUsers_WithUsers() {
        User user1 = new User("001", "user1", null, "metric", "en", false, null);
        User user2 = new User("002", "user2", null, "metric", "en", false, null);
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user1, user2));

        userManagementService.viewAllUsers();

        System.out.println("user1 (ID: 001)");
        System.out.println("user2 (ID: 002)");
    }
}
