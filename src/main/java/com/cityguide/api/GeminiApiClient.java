package com.cityguide.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiApiClient {

    private static final String API_KEY = "AIzaSyBW-midX5Qid5eC5ly6cjTdhpW3FiWvfzM";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();

    public String getCityInfo(String city, String topic) {
        try {
            String prompt = createPrompt(city, topic);
            String requestBody = buildRequestBody(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                return "Error: Received response code " + response.statusCode() + "\n" + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String createPrompt(String city, String topic) {
        switch (topic.toLowerCase()) {
            case "famous food":
                return "List the most famous and traditional food in " + city + ".";
            case "famous tourist spots":
                return "What are the must-visit tourist spots in " + city + "?";
            case "famous restaurants":
                return "Recommend some famous restaurants in " + city + ", including their specialties.";
            case "culture":
                return "Describe the local culture and traditions of " + city + ".";
            case "nightlife":
                return "What are the best pubs and nightlife spots in " + city + "?";
            case "shopping":
                return "Provide insights into the best shopping malls and markets in " + city + ".";
            default:
                return "Tell me something interesting about " + city + ".";
        }
    }

    private String buildRequestBody(String prompt) {
        // Basic JSON construction without a library
        return "{\"contents\":[{\"parts\":[{\"text\":\"" + escapeJson(prompt) + "\"}]}]}";
    }

    private String parseResponse(String responseBody) {
        // Basic JSON parsing without a library
        try {
            String textStartMarker = "\"text\": \"";
            int startIndex = responseBody.indexOf(textStartMarker);
            if (startIndex == -1) {
                return "Error: Could not find 'text' in response.";
            }
            startIndex += textStartMarker.length();

            int endIndex = responseBody.indexOf("\"", startIndex);
            if (endIndex == -1) {
                return "Error: Could not find closing quote for 'text'.";
            }

            // The actual text is here. It's escaped JSON, so we need to unescape it.
            String text = responseBody.substring(startIndex, endIndex);
            return unescapeJson(text);
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private String unescapeJson(String text) {
        return text.replace("\\\\", "\\")
                   .replace("\\\"", "\"")
                   .replace("\\b", "\b")
                   .replace("\\f", "\f")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }
}
