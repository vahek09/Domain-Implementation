package com.weather_service.main;

import com.weather_service.PooledDataSource;
import com.weather_service.domain.User;
import com.weather_service.domain.WeatherData;
import com.weather_service.repository.UserDAO;
import com.weather_service.utility.JdbcUtility;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedApp {

    private final JdbcUtility jdbcUtility;
    private final UserDAO userDAO;

    public MultiThreadedApp(JdbcUtility jdbcUtility, UserDAO userDAO) {
        this.jdbcUtility = jdbcUtility;
        this.userDAO = userDAO;
    }

    public static void main(String[] args) throws InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource(
                "jdbc:postgresql://localhost:5432/postgres", "vahe", "vahe2003"
        );

        JdbcUtility jdbcUtility = new JdbcUtility(pooledDataSource);
        UserDAO userDAO = new UserDAO(jdbcUtility);

        MultiThreadedApp app = new MultiThreadedApp(jdbcUtility, userDAO);
        app.runMultiThreadedTask();
    }

    public void simulateLongAction(String threadId) {
        try {
            // Create a new user
            User user = new User("user" + threadId, "username" + threadId, null, "Celsius", "EN", false, "api_key_" + threadId);
            user.setApiCallCount(5);
            user.setLastApiCallTime(LocalDateTime.now());

            // Create weather data
            WeatherData weatherData = new WeatherData();
            weatherData.setTemperature(20.5);
            weatherData.setFeelsLike(18.0);
            weatherData.setTempMin(15.0);
            weatherData.setTempMax(25.0);
            weatherData.setHumidity(60);
            weatherData.setPressure(1010);
            weatherData.setWindSpeed(3.5);
            weatherData.setWindDegree(90);
            weatherData.setVisibility(10000);
            weatherData.setRain1h(0.0);
            weatherData.setCloudiness(50);
            weatherData.setTimestamp(System.currentTimeMillis());
            weatherData.setWeatherDescription("Clear sky");
            weatherData.setWeatherIcon("01d");

            // Location ID (should come from a valid location in your database)
            int locationId = 1;

            System.out.println("Thread " + threadId + " is inserting a user without transaction...");
            userDAO.insertUserAndWeatherDataWithoutTransaction(user, weatherData, locationId);

            System.out.println("Thread " + threadId + " is inserting a user with transaction...");
            userDAO.insertUserAndWeatherDataWithTransaction(user, weatherData, locationId);

            // Simulate a long-running query (pg_sleep)
            String query = "SELECT pg_sleep(3)";
            System.out.println("Thread " + threadId + " is executing long-running query...");
            jdbcUtility.execute(query);

            // Fetch the user from the database
            User retrievedUser = userDAO.findById("user" + threadId);
            System.out.println("Thread " + threadId + " retrieved user: " + retrievedUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runMultiThreadedTask() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 10; i++) {
            int threadId = i;
            executor.submit(() -> simulateLongAction(String.valueOf(threadId)));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endTime = System.currentTimeMillis();

        System.out.println("All threads finished in: " + (endTime - startTime) + " ms");

        jdbcUtility.closeDataSource();
    }
}
