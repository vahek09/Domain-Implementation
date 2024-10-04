package com.weather_service.interfaces;

import com.weather_service.domain.Location;
import org.json.JSONException;

public interface GeocodingServiceInterface {

    Location fetchCoordinatesForCity(String cityName) throws Exception;

    String fetchCityNameFromCoordinates(double lat, double lon) throws JSONException;
}
