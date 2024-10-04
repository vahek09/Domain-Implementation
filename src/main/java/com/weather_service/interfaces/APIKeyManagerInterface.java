package com.weather_service.interfaces;

import com.weather_service.domain.User;

public interface APIKeyManagerInterface {
    void assignAPIKey(User user);

    void revokeAPIKey(User user);
}

