package org.technamin.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.technamin.assignment.config.HttpClientConfig;
import org.technamin.assignment.config.RabbitMQConsumer;
import org.technamin.assignment.model.*;
import org.technamin.assignment.service.MongoItemService;
import org.technamin.assignment.service.RabbitMQService;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.toString());
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String UPDATE_CHECK = "Checking Updated :: ";

    public static void main(String[] args) {
        MongoItemService mongoService = new MongoItemService();
        HttpResponse<String> response = HttpClientConfig.INSTANCE.getResponse();

        mapModelFromJson(response.body())
                .stream()
                .distinct()
                .parallel()
                .forEachOrdered((Item info) -> {
                    RabbitMQService.sendLog(new Information(info.getDocId(), UpdateType.SAVE, info.getData()));
                    mongoService.save(info);
                });

        mongoService.sendItemToSaveByRabbit(new ItemSaveDto(466, 99L, "fake_data", "1672816948529"));
        mongoService.sendItemToUpdateByRabbit(new ItemUpdateDto(466, "data", "changed_data"));
        logger.log(Level.WARNING, UPDATE_CHECK, mongoService.find("doc_id", 466).get(0).getData());

        RabbitMQConsumer.defaultConsumerInit();
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
}
