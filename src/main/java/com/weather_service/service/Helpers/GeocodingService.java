package com.weather_service.service.Helpers;

import com.weather_service.domain.Location;
import com.weather_service.interfaces.GeocodingServiceInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeocodingService implements GeocodingServiceInterface {

    private final WeatherApiClient weatherApiClient;
    private final String apiKey;

    public GeocodingService(WeatherApiClient weatherApiClient, String apiKey) {
        this.weatherApiClient = weatherApiClient;
        this.apiKey = apiKey;
    }

    @Override
    public Location fetchCoordinatesForCity(String cityName) throws Exception {
        String apiUrl = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", cityName, apiKey);
        try {
            String jsonResponse = weatherApiClient.makeApiCall(apiUrl);
            return parseLocationFromGeocodingResponse(jsonResponse);
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to fetch coordinates for city: " + cityName);
        }
        return null;
    }

    @Override
    public String fetchCityNameFromCoordinates(double lat, double lon) throws JSONException {
        String apiUrl = String.format("http://api.openweathermap.org/geo/1.0/reverse?lat=%.2f&lon=%.2f&limit=1&appid=%s", lat, lon, apiKey);
        try {
            String jsonResponse = weatherApiClient.makeApiCall(apiUrl);
            return parseCityNameFromReverseGeocodingResponse(jsonResponse);
        } catch (JSONException e) {
            // Re-throw the JSONException so the test can capture it
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to fetch city name for coordinates.");
        }
        return null;
    }

    private Location parseLocationFromGeocodingResponse(String jsonResponse) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        if (jsonArray.length() == 0) {
            throw new JSONException("No location data found in the Geocoding API response.");
        }

        if (jsonArray.length() > 0) {
            JSONObject locationObject = jsonArray.getJSONObject(0);
            Location location = new Location();
            location.setCityName(locationObject.getString("name"));
            location.setLatitude(locationObject.getDouble("lat"));
            location.setLongitude(locationObject.getDouble("lon"));
            return location;
        }
        return null;
    }

    private String parseCityNameFromReverseGeocodingResponse(String jsonResponse) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        if (jsonArray.length() > 0) {
            return jsonArray.getJSONObject(0).getString("name");
        }
        throw new JSONException("No city name found in the reverse Geocoding API response.");
    }
}
