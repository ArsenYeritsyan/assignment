package org.technamin.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.technamin.assignment.config.RabbitMQConsumer;
import org.technamin.assignment.controller.Controller;
import org.technamin.assignment.model.*;
import org.technamin.assignment.service.MongoItemService;
import org.technamin.assignment.service.RabbitMQService;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Application {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Controller controller = new Controller();

    public static void main(String[] args) throws IOException {

        String jsonsUrl = "https://gist.githubusercontent.com/creativearmenia/7d877c390a742afa20ddfaeccd0bfc6c/raw/8d15e878faee3808eb85d8c5be31a14d553f0355/input.json";

        MongoItemService mongoService = new MongoItemService();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(jsonsUrl))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        mapModelFromJson(response.body().toString())
                .stream()
                .distinct()
//                .reduce(0,(a1, a2) -> (a1.equals(a2))   a1: a2))
                .peek((Item info) -> RabbitMQService.sendLog(new Information(info.getDoc_id(), UpdateType.SAVE,info.getData())))
                .forEachOrdered(mongoService::save);

        Controller controller = new Controller();

        controller.sendItemToSave(new ItemSaveDto(466, 99L, "fake_data", "1672816948529"));
        controller.sendItemToUpdate(new ItemUpdateDto(466, "data", "changed_data"));
        System.out.println(mongoService.find("doc_id", 466).get(0).getData());

        RabbitMQConsumer.defaultConsumerInit();

    }

    private static List<Item> mapModelFromJson(String jsonBody) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Item> itemList = List.of();
        try {
            itemList = mapper.readValue(jsonBody, new TypeReference<List<Item>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return itemList;
    }

}
