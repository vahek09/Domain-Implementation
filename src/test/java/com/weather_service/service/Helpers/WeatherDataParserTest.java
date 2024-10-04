package com.weather_service.service.Helpers;

import com.weather_service.domain.WeatherData;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherDataParserTest {

    private WeatherDataParser parser;

    @BeforeEach
    void setUp() {
        parser = new WeatherDataParser();
    }

    @Test
    void testParseWeatherData() throws JSONException {
        String jsonResponse = "{"
                + "\"main\": {"
                + "  \"temp\": 20.5,"
                + "  \"feels_like\": 19.8,"
                + "  \"temp_min\": 18.9,"
                + "  \"temp_max\": 22.1,"
                + "  \"pressure\": 1015,"
                + "  \"humidity\": 65"
                + "},"
                + "\"wind\": {"
                + "  \"speed\": 3.6,"
                + "  \"deg\": 220"
                + "},"
                + "\"visibility\": 10000,"
                + "\"clouds\": {"
                + "  \"all\": 75"
                + "},"
                + "\"rain\": {"
                + "  \"1h\": 0.5"
                + "},"
                + "\"name\": \"London\","
                + "\"dt\": 1623456789,"
                + "\"weather\": [{"
                + "  \"description\": \"scattered clouds\","
                + "  \"icon\": \"03d\""
                + "}]"
                + "}";

        WeatherData result = parser.parseWeatherData(jsonResponse);

        assertEquals(20.5, result.getTemperature());
        assertEquals(19.8, result.getFeelsLike());
        assertEquals(18.9, result.getTempMin());
        assertEquals(22.1, result.getTempMax());
        assertEquals(1015, result.getPressure());
        assertEquals(65, result.getHumidity());
        assertEquals(3.6, result.getWindSpeed());
        assertEquals(220, result.getWindDegree());
        assertEquals(10000, result.getVisibility());
        assertEquals(75, result.getCloudiness());
        assertEquals("London", result.getCityName());
        assertEquals(0.5, result.getRain1h());
        assertEquals(1623456789, result.getTimestamp());
        assertEquals("scattered clouds", result.getWeatherDescription());
        assertEquals("03d", result.getWeatherIcon());
    }

    @Test
    void testParseWeatherDataWithoutRain() throws JSONException {
        String jsonResponse = "{"
                + "\"main\": {"
                + "  \"temp\": 20.5,"
                + "  \"feels_like\": 19.8,"
                + "  \"temp_min\": 18.9,"
                + "  \"temp_max\": 22.1,"
                + "  \"pressure\": 1015,"
                + "  \"humidity\": 65"
                + "},"
                + "\"wind\": {"
                + "  \"speed\": 3.6,"
                + "  \"deg\": 220"
                + "},"
                + "\"visibility\": 10000,"
                + "\"clouds\": {"
                + "  \"all\": 75"
                + "},"
                + "\"name\": \"London\","
                + "\"dt\": 1623456789,"
                + "\"weather\": [{"
                + "  \"description\": \"scattered clouds\","
                + "  \"icon\": \"03d\""
                + "}]"
                + "}";

        WeatherData result = parser.parseWeatherData(jsonResponse);

        assertEquals(0.0, result.getRain1h());
    }

    @Test
    void testParseForecastData() throws JSONException {
        String jsonResponse = "{"
                + "\"list\": ["
                + "  {"
                + "    \"main\": {"
                + "      \"temp\": 20.5,"
                + "      \"feels_like\": 19.8,"
                + "      \"temp_min\": 18.9,"
                + "      \"temp_max\": 22.1,"
                + "      \"pressure\": 1015,"
                + "      \"humidity\": 65"
                + "    },"
                + "    \"wind\": {"
                + "      \"speed\": 3.6,"
                + "      \"deg\": 220"
                + "    },"
                + "    \"visibility\": 10000,"
                + "    \"clouds\": {"
                + "      \"all\": 75"
                + "    },"
                + "    \"dt\": 1623456789,"
                + "    \"weather\": [{"
                + "      \"description\": \"scattered clouds\","
                + "      \"icon\": \"03d\""
                + "    }]"
                + "  },"
                + "  {"
                + "    \"main\": {"
                + "      \"temp\": 22.0,"
                + "      \"feels_like\": 21.5,"
                + "      \"temp_min\": 20.5,"
                + "      \"temp_max\": 23.5,"
                + "      \"pressure\": 1014,"
                + "      \"humidity\": 60"
                + "    },"
                + "    \"wind\": {"
                + "      \"speed\": 4.1,"
                + "      \"deg\": 230"
                + "    },"
                + "    \"visibility\": 10000,"
                + "    \"clouds\": {"
                + "      \"all\": 50"
                + "    },"
                + "    \"dt\": 1623460389,"
                + "    \"weather\": [{"
                + "      \"description\": \"partly cloudy\","
                + "      \"icon\": \"02d\""
                + "    }]"
                + "  }"
                + "]"
                + "}";

        List<WeatherData> result = parser.parseForecastData(jsonResponse);

        assertEquals(2, result.size());

        WeatherData firstForecast = result.get(0);
        assertEquals(20.5, firstForecast.getTemperature());
        assertEquals(19.8, firstForecast.getFeelsLike());
        assertEquals(18.9, firstForecast.getTempMin());
        assertEquals(22.1, firstForecast.getTempMax());
        assertEquals(1015, firstForecast.getPressure());
        assertEquals(65, firstForecast.getHumidity());
        assertEquals(3.6, firstForecast.getWindSpeed());
        assertEquals(220, firstForecast.getWindDegree());
        assertEquals(10000, firstForecast.getVisibility());
        assertEquals(75, firstForecast.getCloudiness());
        assertEquals(1623456789, firstForecast.getTimestamp());
        assertEquals("scattered clouds", firstForecast.getWeatherDescription());
        assertEquals("03d", firstForecast.getWeatherIcon());

        WeatherData secondForecast = result.get(1);
        assertEquals(22.0, secondForecast.getTemperature());
        assertEquals(21.5, secondForecast.getFeelsLike());
        assertEquals(20.5, secondForecast.getTempMin());
        assertEquals(23.5, secondForecast.getTempMax());
        assertEquals(1014, secondForecast.getPressure());
        assertEquals(60, secondForecast.getHumidity());
        assertEquals(4.1, secondForecast.getWindSpeed());
        assertEquals(230, secondForecast.getWindDegree());
        assertEquals(10000, secondForecast.getVisibility());
        assertEquals(50, secondForecast.getCloudiness());
        assertEquals(1623460389, secondForecast.getTimestamp());
        assertEquals("partly cloudy", secondForecast.getWeatherDescription());
        assertEquals("02d", secondForecast.getWeatherIcon());
    }

    @Test
    void testParseWeatherDataInvalidJson() {
        String invalidJson = "{ invalid json }";
        assertThrows(JSONException.class, () -> parser.parseWeatherData(invalidJson));
    }

    @Test
    void testParseForecastDataInvalidJson() {
        String invalidJson = "{ invalid json }";
        assertThrows(JSONException.class, () -> parser.parseForecastData(invalidJson));
    }
}