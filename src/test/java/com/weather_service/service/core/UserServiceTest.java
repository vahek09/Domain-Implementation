package com.weather_service.service.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.weather_service.domain.Location;
import com.weather_service.domain.User;
import com.weather_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    // Test case for creating a new user
    @Test
    public void testCreateUser_success() {
        Location location = new Location(40.7128, -74.0060, "New York");
        String userId = "001";
        String username = "testUser";

        // Simulate no user with this ID or username exists
        when(userRepository.userExistsById(userId)).thenReturn(false);
        when(userRepository.userExistsByUsername(username)).thenReturn(false);

        // Act
        userService.createUser(userId, username, location, false);

        // Assert: Ensure saveUser is called once
        verify(userRepository, times(1)).saveUser(any(User.class));
    }

    // Test case when user already exists by ID
    @Test
    public void testCreateUser_userAlreadyExistsById() {
        // Arrange: Simulate a user already exists with this ID
        when(userRepository.userExistsById("001")).thenReturn(true);

        // Act
        userService.createUser("001", "existingUser", null, false);

        // Assert: Ensure saveUser is not called since the user exists by ID
        verify(userRepository, never()).saveUser(any(User.class));
    }

    // Test case when user already exists by username
    @Test
    public void testCreateUser_userAlreadyExistsByUsername() {
        // Arrange: Simulate a user already exists with this username
        when(userRepository.userExistsByUsername("existingUser")).thenReturn(true);

        // Act
        userService.createUser("002", "existingUser", null, false);

        // Assert: Ensure saveUser is not called since the user exists by username
        verify(userRepository, never()).saveUser(any(User.class));
    }

    // Test case for finding a user by ID
    @Test
    public void testFindUserById() {
        // Arrange: Simulate a user exists with this ID
        User user = new User("001", "testUser", null, "metric", "en", false, null);
        when(userRepository.findById("001")).thenReturn(user);

        // Act: Find the user
        User foundUser = userService.findUser("001");

        // Assert: Verify the returned user's details
        assertEquals("testUser", foundUser.getUsername());
    }

    // Test case for finding a user by username
    @Test
    public void testFindUserByUsername() {
        // Arrange: Simulate a user exists with this username
        User user = new User("001", "testUser", null, "metric", "en", false, null);
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        // Act: Find the user by username
        User foundUser = userService.findUserByUsername("testUser");

        // Assert: Verify the returned user's details
        assertEquals("testUser", foundUser.getUsername());
    }

    // Test case for updating an existing user
    @Test
    public void testUpdateUser() {
        // Arrange
        User user = new User("001", "testUser", null, "metric", "en", false, null);
        when(userRepository.findById("001")).thenReturn(user);

        // Act
        userService.updateUser(user);

        // Assert: Ensure the user is updated in the repository
        verify(userRepository, times(1)).saveUser(user); // saveUser is used for updates
    }

    // Test case for updating a non-existing user
    @Test
    public void testUpdateUser_userDoesNotExist() {
        // Arrange
        User user = new User("002", "nonExistingUser", null, "metric", "en", false, null);
        when(userRepository.findById("002")).thenReturn(null);  // User does not exist

        // Act
        userService.updateUser(user);

        // Assert: Ensure saveUser is called for the new user (since it's effectively a new entry)
        verify(userRepository, times(1)).saveUser(user);
    }

    // Test case for deleting a user
    @Test
    public void testDeleteUser_success() {
        // Act
        userService.deleteUser("001");

        // Assert: Ensure the user is deleted from the repository
        verify(userRepository, times(1)).deleteUser("001");
    }
    @Test
    public void testListAllUsers() {
        // Arrange: Set up mock data for the users
        User user1 = new User("001", "testUser1", null, "metric", "en", false, null);
        User user2 = new User("002", "testUser2", null, "metric", "en", false, null);
        Collection<User> mockUsers = Arrays.asList(user1, user2);

        // Simulate the repository returning all users
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act: Call the listAllUsers method
        Collection<User> result = userService.listAllUsers();

        // Assert: Ensure that the correct users are returned and findAll was called once
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        verify(userRepository, times(1)).findAll();
    }
}
