package com.weather_service.utility;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.StringReader;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcUtilityTest {

    private JdbcUtility jdbcUtility;

    @BeforeAll
    public static void initDatabase() throws SQLException {
        // Initialize the H2 database with the same schema and data as Postgres
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        RunScript.execute(connection, new StringReader(
                "CREATE TABLE users (" +
                        "user_id INT PRIMARY KEY, " +
                        "username VARCHAR(100));" +
                        "INSERT INTO users (user_id, username) VALUES (1, 'JohnDoe'), (2, 'JaneDoe');"
        ));
        connection.close();
    }

    @BeforeEach
    public void setUp() {
        jdbcUtility = new JdbcUtility("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
    }

    @Test
    public void testFindOneUser() {
        String query = "SELECT * FROM users WHERE user_id = ?";

        User user = jdbcUtility.findOne(query, rs -> {
            try {
                return new User(rs.getInt("user_id"), rs.getString("username"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 1);

        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("JohnDoe", user.getUsername());
    }

    @Test
    public void testFindOneNonExistentUser() {
        String query = "SELECT * FROM users WHERE user_id = ?";

        User user = jdbcUtility.findOne(query, rs -> {
            try {
                return new User(rs.getInt("user_id"), rs.getString("username"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 999);

        assertNull(user);
    }

    @Test
    public void testFindManyUsers() {
        String query = "SELECT * FROM users";

        List<User> users = jdbcUtility.findMany(query, rs -> {
            try {
                return new User(rs.getInt("user_id"), rs.getString("username"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("JohnDoe", users.get(0).getUsername());
    }

    @Test
    public void testInsertUser() {
        String query = "INSERT INTO users (user_id, username) VALUES (?, ?)";

        jdbcUtility.execute(query, 3, "MikeDoe");

        User user = jdbcUtility.findOne("SELECT * FROM users WHERE user_id = ?", rs -> {
            try {
                return new User(rs.getInt("user_id"), rs.getString("username"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 3);
        assertNotNull(user);
        assertEquals("MikeDoe", user.getUsername());
    }

    @Test
    public void testUpdateUser() {
        String query = "UPDATE users SET username = ? WHERE user_id = ?";

        jdbcUtility.execute(query, "JohnUpdated", 1);

        User user = jdbcUtility.findOne("SELECT * FROM users WHERE user_id = ?", rs -> {
            try {
                return new User(rs.getInt("user_id"), rs.getString("username"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 1);
        assertNotNull(user);
        assertEquals("JohnUpdated", user.getUsername());
    }

    @Test
    public void testDeleteUser() {
        String query = "DELETE FROM users WHERE user_id = ?";

        jdbcUtility.execute(query, 2);

        User user = jdbcUtility.findOne("SELECT * FROM users WHERE user_id = ?", rs -> {
            try {
                return new User(rs.getInt("user_id"), rs.getString("username"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 2);
        assertNull(user);
    }
}

// Simple User class for testing
class User {
    private int userId;
    private String username;

    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
