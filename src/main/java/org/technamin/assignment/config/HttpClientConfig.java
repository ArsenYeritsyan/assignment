package org.technamin.assignment.config;

import org.technamin.assignment.Application;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum HttpClientConfig {
    INSTANCE;

    private static final String JSON_URL = "https://gist.githubusercontent.com/creativearmenia/7d877c390a742afa20ddfaeccd0bfc6c/raw/8d15e878faee3808eb85d8c5be31a14d553f0355/input.json";
    private static final Logger logger = Logger.getLogger(Application.class.toString());
    private final HttpResponse<String> response;

    HttpClientConfig() {
        this.response = getHttpResponse(getHttpRequest());
    }

    private HttpResponse<String> getHttpResponse(HttpRequest request) {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        do {
            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                logger.log(Level.WARNING, e.getCause().toString());
            }
        } while (response != null && response.body().isBlank());
        return response;
    }

    private HttpRequest getHttpRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(JSON_URL))
                .GET()
                .build();
    }

    public HttpResponse<String> getResponse() {
        return this.response;
    }
}
