package com.cityguide.backend;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;
import java.util.HashMap;

@Service
public class GeminiService {

    // IMPORTANT: Replace "YOUR_GEMINI_API_KEY" with your actual Gemini API Key
    private static final String GEMINI_API_KEY = "YOUR_GEMINI_API_KEY";

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + GEMINI_API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getCityInfo(String city, String category) {
        // Using a placeholder for the API key as it's not provided yet.
        // In a real application, this would make an actual API call.
        if (GEMINI_API_KEY.equals("YOUR_GEMINI_API_KEY")) {
            return getPlaceholderResponse(city, category);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", "Tell me about the " + category + " in " + city);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", new Object[]{textPart});

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", new Object[]{content});

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            String response = restTemplate.postForObject(GEMINI_API_URL, requestEntity, String.class);
            // Parse the response to extract the content
            JsonNode root = objectMapper.readTree(response);
            JsonNode textNode = root.at("/candidates/0/content/parts/0/text");
            return textNode.asText();
        } catch (Exception e) {
            // Basic error handling
            e.printStackTrace();
            return "Error fetching data from Gemini API. Please check your API key and network connection.";
        }
    }

    private String getPlaceholderResponse(String city, String category) {
        return switch (category.toLowerCase()) {
            case "famous food" -> "In " + city + ", you must try the delicious local dishes. The cuisine is known for its rich flavors and unique ingredients. A placeholder response while the Gemini API is not connected.";
            case "famous tourist spots" -> "Explore the beautiful sights of " + city + ". From historical landmarks to stunning natural parks, there is something for everyone. A placeholder response while the Gemini API is not connected.";
            case "famous restaurants" -> city + " boasts a vibrant dining scene with many world-class restaurants. A placeholder response while the Gemini API is not connected.";
            case "culture of the region" -> "The culture of " + city + " is rich and diverse, with a fascinating history and vibrant traditions. A placeholder response while the Gemini API is not connected.";
            case "nightlife and famous pubs" -> "Experience the exciting nightlife of " + city + " with its many pubs and clubs. A placeholder response while the Gemini API is not connected.";
            case "shopping mall insights" -> "Shop till you drop in " + city + ". The city offers a wide range of shopping experiences, from luxury boutiques to bustling markets. A placeholder response while the Gemini API is not connected.";
            default -> "Information about " + category + " in " + city + " is not available.";
        };
    }
}
