package com.weather_service.main;

import com.weather_service.service.core.AppService;

public class App {

    public static void main(String[] args) throws Exception {
        AppService appService = new AppService();
        appService.initializeComponents();
        appService.runApp();
    }
}
