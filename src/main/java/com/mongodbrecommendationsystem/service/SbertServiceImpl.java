package com.mongodbrecommendationsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class SbertServiceImpl implements SbertService {
    public double[] encode(String text) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(Map.of("text", text));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/encode"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> result = mapper.readValue(response.body(), Map.class);
            var list = (List<Double>) result.get("embedding");

            double[] embedding = new double[list.size()];
            for (int i = 0; i < list.size(); i++) embedding[i] = list.get(i);

            return embedding;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel SBERT", e);
        }
    }
}
