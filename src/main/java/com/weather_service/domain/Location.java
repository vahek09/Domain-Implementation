package com.weather_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private double latitude;
    private double longitude;
    private String cityName;

    public void setCoordinates(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public String getLocationData() {
        return "Location: " + cityName + " (Lat: " + latitude + ", Lon: " + longitude + ")";
    }
}
