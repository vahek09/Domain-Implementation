package com.weather_service.utility;

import com.weather_service.PooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JdbcUtility {

    private final PooledDataSource pooledDataSource;

    public JdbcUtility(PooledDataSource pooledDataSource) {
        this.pooledDataSource = pooledDataSource;
    }

    public Connection getConnection() throws SQLException {
        return pooledDataSource.getConnection();
    }

    public void execute(String query, Object... args) {
        try (Connection connection = pooledDataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            setPreparedStatementParameters(pstmt, args);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void execute(Connection connection, String query, Object... args) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            setPreparedStatementParameters(pstmt, args);
            pstmt.execute();
        }
    }

    public <T> T findOne(String query, Function<ResultSet, T> mapper, Object... args) {
        try (Connection connection = pooledDataSource.getConnection();
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

    public <T> List<T> findMany(String query, Function<ResultSet, T> mapper, Object... args) {
        List<T> results = new ArrayList<>();
        try (Connection connection = pooledDataSource.getConnection();
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

    private void setPreparedStatementParameters(PreparedStatement pstmt, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
    }

    // Close the pooled data source
    public void closeDataSource() {
        pooledDataSource.close();
    }
}
