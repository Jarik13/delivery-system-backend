package org.deliverysystem.com.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CityCoordinatesLoader {
    private static final Map<String, double[]> COORDINATES = new HashMap<>();

    @PostConstruct
    public void loadCoordinates() {
        try {
            ClassPathResource resource = new ClassPathResource("city-coordinates.csv");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        String cityName = parts[0].trim();
                        double lat = Double.parseDouble(parts[1].trim());
                        double lng = Double.parseDouble(parts[2].trim());

                        COORDINATES.put(cityName.toLowerCase(), new double[]{lat, lng});
                        count++;
                    }
                }
                log.info("Завантажено {} координат міст", count);
            }
        } catch (Exception e) {
            log.error("Помилка завантаження координат міст", e);
        }
    }

    public static double[] getCoordinates(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return null;
        }

        String key = cityName.toLowerCase().trim();

        double[] coords = COORDINATES.get(key);
        if (coords != null) {
            return coords;
        }

        for (Map.Entry<String, double[]> entry : COORDINATES.entrySet()) {
            if (entry.getKey().contains(key) || key.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        String stripped = key.replaceAll("^(місто|село|селище|смт)\\s+", "");
        coords = COORDINATES.get(stripped);
        return coords;
    }
}