package com.weather_service.repository;

import com.weather_service.domain.Location;
import com.weather_service.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    public void testSaveAndFindUserById() {
        // Arrange
        Location location = new Location(40.7128, -74.0060, "New York");
        User user = new User("001", "testUser", location, "metric", "en", false, "apikey");

        // Act
        userRepository.saveUser(user);
        User foundUser = userRepository.findById("001");

        // Assert
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
        assertEquals("New York", foundUser.getLocation().getCityName());
    }

    @Test
    public void testSaveAndFindUserByUsername() {
        // Arrange
        Location location = new Location(40.7128, -74.0060, "New York");
        User user = new User("001", "testUser", location, "metric", "en", false, "apikey");

        // Act
        userRepository.saveUser(user);
        User foundUser = userRepository.findByUsername("testUser");

        // Assert
        assertNotNull(foundUser);
        assertEquals("001", foundUser.getUserID());
    }

    @Test
    public void testUserExistsById_True() {
        // Arrange
        User user = new User("001", "testUser", null, "metric", "en", false, "apikey");
        userRepository.saveUser(user);

        // Act
        boolean result = userRepository.userExistsById("001");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testUserExistsById_False() {
        // Act
        boolean result = userRepository.userExistsById("002");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testUserExistsByUsername_True() {
        // Arrange
        User user = new User("001", "testUser", null, "metric", "en", false, "apikey");
        userRepository.saveUser(user);

        // Act
        boolean result = userRepository.userExistsByUsername("testUser");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testUserExistsByUsername_False() {
        // Act
        boolean result = userRepository.userExistsByUsername("nonexistentUser");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        User user = new User("001", "testUser", null, "metric", "en", false, "apikey");
        userRepository.saveUser(user);

        // Act
        userRepository.deleteUser("001");
        User foundUser = userRepository.findById("001");

        // Assert
        assertNull(foundUser);
        assertFalse(userRepository.userExistsById("001"));
        assertFalse(userRepository.userExistsByUsername("testUser"));
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        User user1 = new User("001", "user1", null, "metric", "en", false, "apikey1");
        User user2 = new User("002", "user2", null, "metric", "en", false, "apikey2");

        userRepository.saveUser(user1);
        userRepository.saveUser(user2);

        // Act
        Collection<User> allUsers = userRepository.findAll();

        // Assert
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    public void testFindAllUsers_Empty() {
        // Act
        Collection<User> allUsers = userRepository.findAll();

        // Assert
        assertTrue(allUsers.isEmpty());
    }
}
