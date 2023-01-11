package org.technamin.assignment.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.technamin.assignment.exceptions.ItemProcessException;
import org.technamin.assignment.model.Item;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public enum ItemsRepository {
    INSTANCE;

    private static final String JSON_URL = "https://gist.githubusercontent.com/creativearmenia/7d877c390a742afa20ddfaeccd0bfc6c/raw/8d15e878faee3808eb85d8c5be31a14d553f0355/input.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    private final List<Item> response;

    ItemsRepository() {
        try {
            this.response = mapModelFromJson(getHttpResponse(getHttpRequest()).body());
        } catch (IOException e) {
            throw new ItemProcessException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ItemProcessException(e);
        }
    }

    private static List<Item> mapModelFromJson(String jsonBody) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonBody, new TypeReference<>() {
        });
    }

    public List<Item> getItems() {
        return this.response;
    }

    private HttpResponse<String> getHttpResponse(HttpRequest request) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }

    private HttpRequest getHttpRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(JSON_URL))
                .GET()
                .build();
    }
}
