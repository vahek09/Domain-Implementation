package com.weather_service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SystemSettings {
    private String preferredLanguage;
    private String preferredUnits;

    public SystemSettings(String preferredLanguage, String preferredUnits) {
        this.preferredLanguage = preferredLanguage;
        this.preferredUnits = preferredUnits;
    }

}
