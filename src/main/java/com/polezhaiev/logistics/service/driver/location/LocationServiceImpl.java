package com.polezhaiev.logistics.service.driver.location;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polezhaiev.logistics.dto.driver.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?format=json&addressdetails=1&q=";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<LocationResponseDto> getCoordinates(String location) {
        try {
            // Убираем запятые, если они есть, и кодируем в URL-формат
            String formattedLocation = location.replace(",", " ");
            String encodedLocation = URLEncoder.encode(formattedLocation, StandardCharsets.UTF_8);

            // Отправляем запрос в OSM
            String response = restTemplate.getForObject(NOMINATIM_URL + encodedLocation, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);

            // Проверяем, есть ли найденные локации
            if (jsonNode.isArray() && !jsonNode.isEmpty()) {
                // Берем первую найденную локацию (самую релевантную)
                JsonNode firstResult = jsonNode.get(0);
                double latitude = firstResult.get("lat").asDouble();
                double longitude = firstResult.get("lon").asDouble();
                String displayName = firstResult.get("display_name").asText();

                return Optional.of(LocationResponseDto.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .displayName(displayName)
                        .build());
            } else {
                System.err.println("Location not found: " + location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
