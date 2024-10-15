package com.weather_service.repository;

import com.weather_service.domain.Location;
import com.weather_service.utility.JdbcUtility;

import java.sql.ResultSet;
import java.util.List;

public class LocationDAO {

    private final JdbcUtility jdbcUtility;

    public LocationDAO(JdbcUtility jdbcUtility) {
        this.jdbcUtility = jdbcUtility;
    }

    public void createLocation(Location location) {
        String query = "INSERT INTO locations (city_name, latitude, longitude) VALUES (?, ?, ?)";
        jdbcUtility.execute(query, location.getCityName(), location.getLatitude(), location.getLongitude());
    }

    public Location findById(int locationId) {
        String query = "SELECT * FROM locations WHERE location_id = ?";
        return jdbcUtility.findOne(query, this::mapRowToLocation, locationId);
    }

    public List<Location> findAll() {
        String query = "SELECT * FROM locations";
        return jdbcUtility.findMany(query, this::mapRowToLocation);
    }

    public void updateLocationByUserId(Location location, int userId) {
        String query = "UPDATE locations " +
                "SET city_name = ?, latitude = ?, longitude = ? " +
                "WHERE location_id = (SELECT location_id FROM weather_data WHERE user_id = ?)";

        jdbcUtility.execute(query, location.getCityName(), location.getLatitude(), location.getLongitude(), userId);
    }


    public void deleteLocation(int locationId) {
        String query = "DELETE FROM locations WHERE location_id = ?";
        jdbcUtility.execute(query, locationId);
    }

    // Map a ResultSet row to a Location object
    private Location mapRowToLocation(ResultSet rs) {
        try {
            return new Location(
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getString("city_name")
            );
        } catch (Exception e) {
            throw new RuntimeException("Error mapping row to Location", e);
        }
    }
}
