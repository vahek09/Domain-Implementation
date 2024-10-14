package com.weather_service.utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class JdbcUtility {

    private final String DB_URL;
    private final String USER;
    private final String PASS;

    // Constructor to initialize DB connection details
    public JdbcUtility(String dbUrl, String user, String pass) {
        this.DB_URL = dbUrl;
        this.USER = user;
        this.PASS = pass;
    }
//
//    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
//    private static final String USER = "vahe";
//    private static final String PASS = "vahe2003";

    // Method to execute queries with no results, using Object... args
    public void execute(String query, Object... args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            setPreparedStatementParameters(pstmt, args);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to execute queries with no results, using Consumer to expose PreparedStatement
    public void execute(String query, Consumer<PreparedStatement> statementConsumer) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            statementConsumer.accept(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to run a query and return one result
    public <T> T findOne(String query, Function<ResultSet, T> mapper, Object... args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            setPreparedStatementParameters(pstmt, args);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return null;
            }
            T result = mapper.apply(rs);

            if (rs.next()) {
                throw new RuntimeException("Expected one result, but got more than one.");
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to run a query and return a list of results
    public <T> List<T> findMany(String query, Function<ResultSet, T> mapper, Object... args) {
        List<T> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            setPreparedStatementParameters(pstmt, args);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(mapper.apply(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Helper method to set parameters for the PreparedStatement
    private void setPreparedStatementParameters(PreparedStatement pstmt, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
    }
}
