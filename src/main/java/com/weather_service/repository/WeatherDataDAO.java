package com.weather_service.repository;

import com.weather_service.domain.WeatherData;
import com.weather_service.utility.JdbcUtility;

import java.sql.ResultSet;
import java.util.List;

public class WeatherDataDAO {

    private final JdbcUtility jdbcUtility;

    public WeatherDataDAO(JdbcUtility jdbcUtility) {
        this.jdbcUtility = jdbcUtility;
    }

    public void createWeatherData(WeatherData weatherData, int locationId, String userId) {
        String query = "INSERT INTO weather_data (location_id, user_id, temperature, feels_like, temp_min, temp_max, humidity, pressure, wind_speed, wind_degree, visibility, rain_1h, cloudiness, timestamp, weather_description, weather_icon) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcUtility.execute(query, locationId, userId, weatherData.getTemperature(), weatherData.getFeelsLike(), weatherData.getTempMin(), weatherData.getTempMax(),
                weatherData.getHumidity(), weatherData.getPressure(), weatherData.getWindSpeed(), weatherData.getWindDegree(),
                weatherData.getVisibility(), weatherData.getRain1h(), weatherData.getCloudiness(), weatherData.getTimestamp(),
                weatherData.getWeatherDescription(), weatherData.getWeatherIcon());
    }

    public WeatherData findById(int weatherDataId) {
        String query = "SELECT * FROM weather_data WHERE weather_data_id = ?";
        return jdbcUtility.findOne(query, this::mapRowToWeatherData, weatherDataId);
    }

    public List<WeatherData> findAll() {
        String query = "SELECT * FROM weather_data";
        return jdbcUtility.findMany(query, this::mapRowToWeatherData);
    }

    public void deleteWeatherData(int weatherDataId) {
        String query = "DELETE FROM weather_data WHERE weather_data_id = ?";
        jdbcUtility.execute(query, weatherDataId);
    }

    // Helper method to map a ResultSet row to a WeatherData object
    private WeatherData mapRowToWeatherData(ResultSet rs) {
        try {
            WeatherData weatherData = new WeatherData();
            weatherData.setTemperature(rs.getDouble("temperature"));
            weatherData.setFeelsLike(rs.getDouble("feels_like"));
            weatherData.setTempMin(rs.getDouble("temp_min"));
            weatherData.setTempMax(rs.getDouble("temp_max"));
            weatherData.setHumidity(rs.getInt("humidity"));
            weatherData.setPressure(rs.getInt("pressure"));
            weatherData.setWindSpeed(rs.getDouble("wind_speed"));
            weatherData.setWindDegree(rs.getInt("wind_degree"));
            weatherData.setVisibility(rs.getInt("visibility"));
            weatherData.setRain1h(rs.getDouble("rain_1h"));
            weatherData.setCloudiness(rs.getInt("cloudiness"));
            weatherData.setWeatherDescription(rs.getString("weather_description"));
            weatherData.setWeatherIcon(rs.getString("weather_icon"));
            return weatherData;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping row to WeatherData", e);
        }
    }
}
