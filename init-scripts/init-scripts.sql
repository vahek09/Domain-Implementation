CREATE TABLE IF NOT EXISTS locations (
    location_id SERIAL PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL,
    latitude DECIMAL(9, 6) NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE,
    api_key VARCHAR(100),
    api_call_count INT DEFAULT 0,
    last_api_call_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS weather_data (
    weather_data_id SERIAL PRIMARY KEY,
    location_id INT,
    user_id INT,
    temperature DECIMAL(5, 2),
    feels_like DECIMAL(5, 2),
    temp_min DECIMAL(5, 2),
    temp_max DECIMAL(5, 2),
    humidity INT,
    pressure INT,
    wind_speed DECIMAL(4, 2),
    wind_degree INT,
    visibility INT,
    rain_1h DECIMAL(5, 2),
    cloudiness INT,
    timestamp TIMESTAMP,
    weather_icon VARCHAR(50),
    weather_description VARCHAR(255),
    FOREIGN KEY (location_id) REFERENCES locations(location_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS admins (
    admin_id SERIAL PRIMARY KEY,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS system_settings (
    system_settings_id SERIAL PRIMARY KEY,
    user_id INT,
    preferred_language VARCHAR(10),
    preferred_units VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS manages (
    user_id INT,
    weather_data_id INT,
    PRIMARY KEY (user_id, weather_data_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (weather_data_id) REFERENCES weather_data(weather_data_id)
);

-- Creating indexes
CREATE INDEX IF NOT EXISTS idx_manages_user_id ON manages (user_id);
CREATE INDEX IF NOT EXISTS idx_manages_weather_data_id ON manages (weather_data_id);
