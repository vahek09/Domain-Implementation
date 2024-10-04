package com.weather_service.service.core;

import com.weather_service.domain.Admin;
import com.weather_service.repository.WeatherRepository;
import com.weather_service.service.APIKeyManagementService;
import com.weather_service.service.SystemSettingsService;
import com.weather_service.service.UsageStatisticsService;
import com.weather_service.service.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private AdminService adminService;
    private UserManagementService userManagementService;
    private APIKeyManagementService apiKeyManagementService;
    private SystemSettingsService systemSettingsService;
    private UsageStatisticsService usageStatisticsService;
    private WeatherRepository weatherRepository;

    @BeforeEach
    public void setUp() {
        // Mock the currently logged-in admin
        Admin loggedInAdmin = mock(Admin.class);
        when(loggedInAdmin.getUserID()).thenReturn("admin1");

        userManagementService = mock(UserManagementService.class);
        apiKeyManagementService = mock(APIKeyManagementService.class);
        systemSettingsService = mock(SystemSettingsService.class);
        usageStatisticsService = mock(UsageStatisticsService.class);
        weatherRepository = mock(WeatherRepository.class);

        adminService = new AdminService(loggedInAdmin, userManagementService, apiKeyManagementService, systemSettingsService, usageStatisticsService, weatherRepository);
    }

    @Test
    public void testDisplayAdminMenu_createUser() {
        String input = "1\n8\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        adminService.displayAdminMenu(scanner);

        verify(userManagementService, times(1)).createUser(scanner);
    }

    @Test
    public void testDisplayAdminMenu_manageAPIKeys() {
        String input = "3\n8\n";  // Select option 3 to manage API keys, then exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        adminService.displayAdminMenu(scanner);

        verify(apiKeyManagementService, times(1)).manageAPIKeys(scanner);
    }

    @Test
    public void testDisplayAdminMenu_viewUsageStatistics() {
        String input = "4\n8\n";  // Select option 4 to view usage statistics, then exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        adminService.displayAdminMenu(scanner);

        verify(usageStatisticsService, times(1)).viewUsageStatistics();
    }

    @Test
    public void testDisplayAdminMenu_manageSystemSettings() {
        String input = "5\n8\n";  // Select option 5 to manage system settings, then exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        adminService.displayAdminMenu(scanner);

        verify(systemSettingsService, times(1)).manageSystemSettings(scanner);
    }

    @Test
    public void testDisplayAdminMenu_viewAllUsers() {
        String input = "6\n8\n";  // Select option 6 to view all users, then exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        adminService.displayAdminMenu(scanner);

        verify(userManagementService, times(1)).viewAllUsers();
    }

    @Test
    public void testDisplayAdminMenu_clearCache() {
        Scanner scanner = mock(Scanner.class);

        when(scanner.nextInt()).thenReturn(7).thenReturn(8);
        when(scanner.nextLine()).thenReturn("");

        adminService.displayAdminMenu(scanner);

        verify(weatherRepository, times(1)).clearAllWeatherData();
    }

    @Test
    public void testDisplayAdminMenu_exit() {
        String input = "8\n";  // Select option 8 to exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        adminService.displayAdminMenu(scanner);
    }

    @Test
    public void testDisplayAdminMenu_invalidOption() {
        String input = "99\n8\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        adminService.displayAdminMenu(scanner);

        // No specific method should be called, ensure no interaction
        verifyNoInteractions(userManagementService, apiKeyManagementService, usageStatisticsService, systemSettingsService, weatherRepository);
    }
}
