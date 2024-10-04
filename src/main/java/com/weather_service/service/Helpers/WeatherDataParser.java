package com.weather_service.service.Helpers;

import com.weather_service.domain.WeatherData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataParser {

    public WeatherData parseWeatherData(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(jsonObject.getJSONObject("main").getDouble("temp"));
        weatherData.setFeelsLike(jsonObject.getJSONObject("main").getDouble("feels_like"));
        weatherData.setTempMin(jsonObject.getJSONObject("main").getDouble("temp_min"));
        weatherData.setTempMax(jsonObject.getJSONObject("main").getDouble("temp_max"));
        weatherData.setPressure(jsonObject.getJSONObject("main").getInt("pressure"));
        weatherData.setHumidity(jsonObject.getJSONObject("main").getInt("humidity"));
        weatherData.setWindSpeed(jsonObject.getJSONObject("wind").getDouble("speed"));
        weatherData.setWindDegree(jsonObject.getJSONObject("wind").getInt("deg"));
        weatherData.setVisibility(jsonObject.getInt("visibility"));
        weatherData.setCloudiness(jsonObject.getJSONObject("clouds").getInt("all"));
        weatherData.setCityName(jsonObject.getString("name"));

        if (jsonObject.has("rain") && jsonObject.getJSONObject("rain").has("1h")) {
            weatherData.setRain1h(jsonObject.getJSONObject("rain").getDouble("1h"));
        }

        weatherData.setTimestamp(jsonObject.getLong("dt"));
        weatherData.setWeatherDescription(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
        weatherData.setWeatherIcon(jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon"));

        return weatherData;
    }

    public List<WeatherData> parseForecastData(String jsonResponse) throws JSONException {
        List<WeatherData> forecastList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray listArray = jsonObject.getJSONArray("list");

        for (int i = 0; i < listArray.length(); i++) {
            JSONObject forecastObject = listArray.getJSONObject(i);
            WeatherData weatherData = new WeatherData();

            weatherData.setTemperature(forecastObject.getJSONObject("main").getDouble("temp"));
            weatherData.setFeelsLike(forecastObject.getJSONObject("main").getDouble("feels_like"));
            weatherData.setTempMin(forecastObject.getJSONObject("main").getDouble("temp_min"));
            weatherData.setTempMax(forecastObject.getJSONObject("main").getDouble("temp_max"));
            weatherData.setPressure(forecastObject.getJSONObject("main").getInt("pressure"));
            weatherData.setHumidity(forecastObject.getJSONObject("main").getInt("humidity"));
            weatherData.setWindSpeed(forecastObject.getJSONObject("wind").getDouble("speed"));
            weatherData.setWindDegree(forecastObject.getJSONObject("wind").getInt("deg"));
            weatherData.setVisibility(forecastObject.getInt("visibility"));
            weatherData.setCloudiness(forecastObject.getJSONObject("clouds").getInt("all"));
            weatherData.setTimestamp(forecastObject.getLong("dt"));
            weatherData.setWeatherDescription(forecastObject.getJSONArray("weather").getJSONObject(0).getString("description"));
            weatherData.setWeatherIcon(forecastObject.getJSONArray("weather").getJSONObject(0).getString("icon"));

            forecastList.add(weatherData);
        }

        return forecastList;
    }
}
