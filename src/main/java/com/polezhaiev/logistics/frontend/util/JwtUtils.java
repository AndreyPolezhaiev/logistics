package com.polezhaiev.logistics.frontend.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class JwtUtils {

    public static Map<String, Object> decode(String jwtToken) {
        try {
            String[] parts = jwtToken.split("\\.");
            String payload = parts[1];

            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String json = new String(decodedBytes);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Map.class);

        } catch (Exception e) {
            throw new RuntimeException("Can't decode token: " + e.getMessage(), e);
        }
    }
}
