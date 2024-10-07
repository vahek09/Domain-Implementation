package com.weather_service.service.core;

import com.weather_service.domain.*;
import com.weather_service.repository.UserRepository;
import com.weather_service.repository.WeatherRepository;
import com.weather_service.service.*;
import com.weather_service.service.Helpers.GeocodingService;
import com.weather_service.service.Helpers.WeatherApiClient;
import com.weather_service.service.Helpers.WeatherDataParser;
import com.weather_service.utility.InputUtility;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONException;

import java.util.List;
import java.util.Scanner;

public class AppService {

    private static final String API_KEY_ENV = "OPENWEATHER_API_KEY";
    private UserService userService;
    private WeatherService weatherService;
    private AdminService adminService;
    private GeocodingService geocodingService;
    private SettingsManager settingsManager;

    public void initializeComponents() {
        Dotenv dotenv = Dotenv.load();
        String sharedApiKey = dotenv.get(API_KEY_ENV);

        // Initialize repositories
        UserRepository userRepository = new UserRepository();
        WeatherRepository weatherRepository = new WeatherRepository();

        // Initialize services
        userService = new UserService(userRepository);
        WeatherApiClient weatherApiClient = new WeatherApiClient();
        WeatherDataParser weatherDataParser = new WeatherDataParser();
        geocodingService = new GeocodingService(weatherApiClient, sharedApiKey);
        weatherService = new WeatherService(weatherRepository, weatherApiClient, weatherDataParser, geocodingService, sharedApiKey);

        // Initialize service helpers
        APIKeyManager apiKeyManager = new APIKeyManager(userService);
        settingsManager = new SettingsManager(userService);

        // Initialize management services
        UserManagementService userManagementService = new UserManagementService(userService);
        APIKeyManagementService apiKeyManagementService = new APIKeyManagementService(apiKeyManager, userService);
        SystemSettingsService systemSettingsService = new SystemSettingsService(settingsManager, userService);
        UsageStatisticsService usageStatisticsService = new UsageStatisticsService(userService);

        // Initialize the admin user
        Location adminLocation = new Location();
        adminLocation.setCoordinates(40.7128, -74.0060);
        adminLocation.setCityName("New York");

        Admin admin = new Admin("admin1", "AdminUser", adminLocation, "metric", "en", true, sharedApiKey);
        userService.createUser(admin.getUserID(), admin.getUsername(), admin.getLocation(), true);
        adminService = new AdminService(admin, userManagementService, apiKeyManagementService,
                systemSettingsService, usageStatisticsService, weatherRepository);
    }

    public void runApp() throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("------------------------");
            System.out.println("Welcome to the Weather App!");
            System.out.println("1. Set/Update User Location");
            System.out.println("2. Fetch Current Weather");
            System.out.println("3. Fetch Coordinates for a City (Geocoding)");
            System.out.println("4. Fetch City Name for Coordinates (Reverse Geocoding)");
            System.out.println("5. Fetch 5-Day / 3-Hour Forecast");
            System.out.println("6. Check System Settings");
            System.out.println("7. For Admins (Admin Menu)");
            System.out.println("8. Exit");
            System.out.println("------------------------");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    updateUserLocation(scanner);
                    break;
                case 2:
                    fetchCurrentWeather(scanner);
                    break;
                case 3:
                    fetchCoordinatesForCity(scanner);
                    break;
                case 4:
                    fetchCityNameFromCoordinates(scanner);
                    break;
                case 5:
                    fetchFiveDayForecast(scanner);
                    break;
                case 6:
                    checkSystemSettings(scanner);
                    break;
                case 7:
                    if (adminLogin(scanner)) {
                        adminService.displayAdminMenu(scanner);
                    } else {
                        System.out.println("Unauthorized access. Only admins are allowed.");
                    }
                    break;
                case 8:
                    running = false;
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    public boolean adminLogin(Scanner scanner) {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        User user = userService.findUserByUsername(username);

        return user != null && user.isAdmin();
    }

    public void updateUserLocation(Scanner scanner) {
        User user = InputUtility.findUserByIdOrUsername(scanner, userService);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter new city name: ");
        String cityName = scanner.nextLine();

        double latitude = InputUtility.getValidDouble(scanner, "Enter new latitude: ");
        double longitude = InputUtility.getValidDouble(scanner, "Enter new longitude: ");

        Location location = new Location();
        location.setCoordinates(latitude, longitude);
        location.setCityName(cityName);

        user.setLocation(location);
        userService.updateUser(user);

        System.out.println("User location updated to: " + cityName);
    }

    public void fetchCurrentWeather(Scanner scanner) throws Exception {
        User user = InputUtility.findUserByIdOrUsername(scanner, userService);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        WeatherData weatherData = weatherService.fetchCurrentWeather(user);
        if (weatherData != null) {
            System.out.println();
            System.out.println("Current Weather in " + weatherData.getCityName() + ":");
            System.out.println("Temperature: " + weatherData.getTemperature() + " °C");
            System.out.println("Weather: " + weatherData.getWeatherDescription());
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }

    public void fetchFiveDayForecast(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        User user = userService.findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        List<WeatherData> forecastData = weatherService.fetchFiveDayForecast(user);
        if (!forecastData.isEmpty()) {
            System.out.println("5-Day / 3-Hour Forecast for " + user.getLocation().getCityName() + ":");
            for (WeatherData data : forecastData) {
                System.out.println("Timestamp: " + data.getTimestamp());
                System.out.println("Temperature: " + data.getTemperature() + " °C");
                System.out.println("Weather: " + data.getWeatherDescription());
                System.out.println("-------------");
            }
        } else {
            System.out.println("Failed to fetch forecast data.");
        }
    }

    public void fetchCityNameFromCoordinates(Scanner scanner) throws JSONException {
        double latitude = InputUtility.getValidDouble(scanner, "Enter latitude: ");
        double longitude = InputUtility.getValidDouble(scanner, "Enter longitude: ");
        scanner.nextLine();

        String cityName = geocodingService.fetchCityNameFromCoordinates(latitude, longitude);
        if (cityName != null) {
            System.out.println("City name for coordinates (" + latitude + ", " + longitude + "): " + cityName);
        } else {
            System.out.println("Failed to fetch city name for coordinates.");
        }
    }

    public void fetchCoordinatesForCity(Scanner scanner) throws Exception {
        String cityName = InputUtility.getCityName(scanner);

        Location location = geocodingService.fetchCoordinatesForCity(cityName);
        if (location != null) {
            System.out.println("Coordinates for " + cityName + ": Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
        } else {
            System.out.println("Failed to fetch coordinates for " + cityName);
        }
    }

    public void checkSystemSettings(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        User user = userService.findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        SystemSettings settings = settingsManager.getSystemSettings(user);
        System.out.println("System Settings for User ID: " + user.getUserID());
        System.out.println("Preferred Language: " + settings.getPreferredLanguage());
        System.out.println("Preferred Units: " + settings.getPreferredUnits());
    }

}
