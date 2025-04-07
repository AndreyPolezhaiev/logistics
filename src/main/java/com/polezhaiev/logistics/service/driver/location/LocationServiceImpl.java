package com.polezhaiev.logistics.service.driver.location;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polezhaiev.logistics.dto.driver.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<LocationResponseDto> getCoordinates(String location) {
        String encodedLocation = URLEncoder.encode(location.replace(",", " "), StandardCharsets.UTF_8);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("format", "json")
                        .queryParam("addressdetails", "1")
                        .queryParam("q", encodedLocation)
                        .build())
                .header(HttpHeaders.USER_AGENT, "SpringApp")
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(json);
                        if (jsonNode.isArray() && !jsonNode.isEmpty()) {
                            JsonNode first = jsonNode.get(0);
                            return LocationResponseDto.builder()
                                    .latitude(first.get("lat").asDouble())
                                    .longitude(first.get("lon").asDouble())
                                    .displayName(first.get("display_name").asText())
                                    .build();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(result -> result != null);
    }

}
