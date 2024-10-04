package com.weather_service.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SystemSettingsTest {

    @Test
    public void testConstructor() {
        // Arrange
        String preferredLanguage = "en";
        String preferredUnits = "metric";

        // Act
        SystemSettings systemSettings = new SystemSettings(preferredLanguage, preferredUnits);

        // Assert
        assertEquals(preferredLanguage, systemSettings.getPreferredLanguage());
        assertEquals(preferredUnits, systemSettings.getPreferredUnits());
    }

    @Test
    public void testNoArgsConstructorAndSetters() {
        // Arrange
        SystemSettings systemSettings = new SystemSettings();

        // Act
        systemSettings.setPreferredLanguage("fr");
        systemSettings.setPreferredUnits("imperial");

        // Assert
        assertEquals("fr", systemSettings.getPreferredLanguage());
        assertEquals("imperial", systemSettings.getPreferredUnits());
    }

    @Test
    public void testGetters() {
        // Arrange
        SystemSettings systemSettings = new SystemSettings("es", "metric");

        // Act & Assert
        assertEquals("es", systemSettings.getPreferredLanguage());
        assertEquals("metric", systemSettings.getPreferredUnits());
    }
}
