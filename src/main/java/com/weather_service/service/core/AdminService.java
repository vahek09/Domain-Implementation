package com.weather_service.service.core;

import com.weather_service.domain.Admin;
import com.weather_service.repository.WeatherRepository;
import com.weather_service.service.APIKeyManagementService;
import com.weather_service.service.SystemSettingsService;
import com.weather_service.service.UsageStatisticsService;
import com.weather_service.service.UserManagementService;

import java.util.Scanner;

public class AdminService {
    private final UserManagementService userManagementService;
    private final APIKeyManagementService apiKeyManagementService;
    private final SystemSettingsService systemSettingsService;
    private final UsageStatisticsService usageStatisticsService;
    private final WeatherRepository weatherRepository;
    private final Admin loggedInAdmin;

    public AdminService(Admin loggedInAdmin, UserManagementService userManagementService,
                        APIKeyManagementService apiKeyManagementService,
                        SystemSettingsService systemSettingsService,
                        UsageStatisticsService usageStatisticsService,
                        WeatherRepository weatherRepository) {
        this.loggedInAdmin = loggedInAdmin;
        this.userManagementService = userManagementService;
        this.apiKeyManagementService = apiKeyManagementService;
        this.systemSettingsService = systemSettingsService;
        this.usageStatisticsService = usageStatisticsService;
        this.weatherRepository = weatherRepository;
    }

    public void displayAdminMenu(Scanner scanner) {
        boolean adminRunning = true;
        while (adminRunning) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Create User");
            System.out.println("2. Delete User");
            System.out.println("3. Manage API Keys");
            System.out.println("4. View Usage Statistics");
            System.out.println("5. Manage User System Settings");
            System.out.println("6. View All Users");
            System.out.println("7. Clear Cache");
            System.out.println("8. Back to Main Menu");
            System.out.print("Choose an option: ");
            int adminOption = scanner.nextInt();
            scanner.nextLine();

            switch (adminOption) {
                case 1:
                    userManagementService.createUser(scanner);
                    break;
                case 2:
                    userManagementService.deleteUser(scanner, loggedInAdmin);
                    break;
                case 3:
                    apiKeyManagementService.manageAPIKeys(scanner);
                    break;
                case 4:
                    usageStatisticsService.viewUsageStatistics();
                    break;
                case 5:
                    systemSettingsService.manageSystemSettings(scanner);
                    break;
                case 6:
                    userManagementService.viewAllUsers();
                    break;
                case 7:
                    clearCache();
                    break;
                case 8:
                    adminRunning = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void clearCache() {
        weatherRepository.clearAllWeatherData();
        System.out.println("Cache cleared successfully.");
    }
}
