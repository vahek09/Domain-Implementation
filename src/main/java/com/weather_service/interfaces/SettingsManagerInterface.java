package com.weather_service.interfaces;

import com.weather_service.domain.SystemSettings;
import com.weather_service.domain.User;

public interface SettingsManagerInterface {
    SystemSettings getSystemSettings(User user);

    void updateSettings(User user, String newLanguage, String newUnits);
}
