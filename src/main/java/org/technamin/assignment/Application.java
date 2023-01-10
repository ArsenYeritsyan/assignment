package org.technamin.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.technamin.assignment.config.RabbitMQConsumer;
import org.technamin.assignment.model.*;
import org.technamin.assignment.service.MongoItemService;
import org.technamin.assignment.service.RabbitMQService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
    private static final String JSON_URL = "https://gist.githubusercontent.com/creativearmenia/7d877c390a742afa20ddfaeccd0bfc6c/raw/8d15e878faee3808eb85d8c5be31a14d553f0355/input.json";
    private static final Logger logger = Logger.getLogger(Application.class.toString());
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String UPDATE_CHECK = "Checking Updated :: ";
    private static final Job job = new Job();

    public static void main(String[] args) {
        MongoItemService mongoService = new MongoItemService();
        HttpResponse<String> response = getHttpResponse(getHttpRequest());

        mapModelFromJson(response.body())
                .stream()
                .distinct()
                .forEachOrdered((Item info) -> {
                    RabbitMQService.sendLog(new Information(info.getDocId(), UpdateType.SAVE, info.getData()));
                    mongoService.save(info);
                });

        job.sendItemToSave(new ItemSaveDto(466, 99L, "fake_data", "1672816948529"));
        job.sendItemToUpdate(new ItemUpdateDto(466, "data", "changed_data"));
        logger.log(Level.WARNING, UPDATE_CHECK, mongoService.find("doc_id", 466).get(0).getData());

        RabbitMQConsumer.defaultConsumerInit();
    }

    private static HttpResponse<String> getHttpResponse(HttpRequest request) {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        do {
            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (InterruptedException | IOException e) {
                logger.log(Level.WARNING, e.getCause().toString());
            }
        } while (response != null && !response.body().isBlank());
        return response;
    }

    private static List<Item> mapModelFromJson(String jsonBody) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Item> itemList = List.of();
        try {
            itemList = mapper.readValue(jsonBody, new TypeReference<List<Item>>() {
            });
        } catch (JsonProcessingException e) {
            logger.log(Level.WARNING, e.getCause().toString());
        }
        return itemList;
    }

    private static HttpRequest getHttpRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(JSON_URL))
                .GET()
                .build();
    }
}
