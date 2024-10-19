package com.weather_service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IsolationLevelDemo {

    public static void main(String[] args) {
        System.out.println("___________________________________________");
        demoReadCommittedWithExplainAnalyze();
        System.out.println("___________________________________________");
        demoSerializableIsolationWithExplainAnalyze();
        System.out.println("___________________________________________");
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "vahe", "vahe2003");
    }

    public static void demoReadCommittedWithExplainAnalyze() {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            System.out.println("Default Isolation Level (READ COMMITTED): " + connection.getTransactionIsolation());

            // Start Transaction 1
            System.out.println("Transaction 1: Reading initial data with EXPLAIN ANALYZE...");
            String selectSQL = "EXPLAIN ANALYZE SELECT * FROM weather_data WHERE weather_data_id = 1";
            try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }

            // Start Transaction 2
            new Thread(() -> {
                try (Connection connection2 = getConnection()) {
                    connection2.setAutoCommit(false);
                    connection2.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                    System.out.println("Transaction 2: Inserting new weather_data...");
                    String insertSQL = "INSERT INTO weather_data (location_id, user_id, temperature) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = connection2.prepareStatement(insertSQL)) {
                        stmt.setInt(1, 1);
                        stmt.setInt(2, 1);
                        stmt.setDouble(3, 30.0);
                        stmt.executeUpdate();
                        connection2.commit();
                        System.out.println("Transaction 2: Inserted new weather_data with temperature 30.0");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();

            // Transaction 2 finishes
            Thread.sleep(2000);

            //Transaction 1
            System.out.println("Transaction 1: Performing update based on initial read...");
            String updateSQL = "UPDATE weather_data SET temperature = ? WHERE weather_data_id = 1";
            try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
                stmt.setDouble(1, 25.0);
                stmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Transaction 1: Updated temperature to 25.0");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void demoSerializableIsolationWithExplainAnalyze() {
        try (Connection connection = getConnection()) {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false); // Enable transactions

            System.out.println("Serializable Isolation Level: " + connection.getTransactionIsolation());

            System.out.println("Transaction 1: Reading initial data with EXPLAIN ANALYZE...");
            String selectSQL = "EXPLAIN ANALYZE SELECT * FROM weather_data WHERE weather_data_id = 1";
            try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }

            // Start Transaction 2
            new Thread(() -> {
                try (Connection connection2 = getConnection()) {
                    connection2.setAutoCommit(false);
                    connection2.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                    System.out.println("Transaction 2: Inserting new weather_data...");
                    String insertSQL = "INSERT INTO weather_data (location_id, user_id, temperature) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = connection2.prepareStatement(insertSQL)) {
                        stmt.setInt(1, 1);
                        stmt.setInt(2, 1);
                        stmt.setDouble(3, 30.0);
                        stmt.executeUpdate();
                        connection2.commit();
                        System.out.println("Transaction 2: Inserted new weather_data with temperature 30.0");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();

            Thread.sleep(2000);

            // Continue Transaction 1
            System.out.println("Transaction 1: Performing update based on initial read...");
            String updateSQL = "UPDATE weather_data SET temperature = ? WHERE weather_data_id = 1";
            try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
                stmt.setDouble(1, 25.0);
                stmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Transaction 1: Updated temperature to 25.0");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
