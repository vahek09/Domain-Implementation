package com.weather_service.main;

import com.weather_service.PooledDataSource;
import com.weather_service.utility.JdbcUtility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedApp {

    public static void main(String[] args) throws InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource(
                "jdbc:postgresql://localhost:5432/postgres", "vahe", "vahe2003"
        );

        JdbcUtility jdbcUtility = new JdbcUtility(pooledDataSource);

        MultiThreadedApp app = new MultiThreadedApp(jdbcUtility);
        app.runMultiThreadedTask();
    }

    private final JdbcUtility jdbcUtility;

    public MultiThreadedApp(JdbcUtility jdbcUtility) {
        this.jdbcUtility = jdbcUtility;
    }

    public void simulateLongAction(int threadId) {
        String query = "SELECT pg_sleep(3)";
        System.out.println("Thread " + threadId + " is executing long-running query...");
        jdbcUtility.execute(query);
    }

    public void runMultiThreadedTask() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 10; i++) {
            int threadId = i;
            executor.submit(() -> simulateLongAction(threadId));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endTime = System.currentTimeMillis();

        System.out.println("All threads finished in: " + (endTime - startTime) + " ms");

        jdbcUtility.closeDataSource();
    }
}
