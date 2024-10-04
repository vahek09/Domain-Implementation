package com.weather_service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class WeatherData {
    private double temperature;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private int pressure;
    private int humidity;
    private double windSpeed;
    private int windDegree;
    private int visibility;
    private double rain1h;
    private int cloudiness;
    private long timestamp;
    private String weatherDescription;
    private String weatherIcon;
    private String cityName;

}
