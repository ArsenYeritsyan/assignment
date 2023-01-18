package org.technamin.assignment;

import org.technamin.assignment.config.RabbitMQConsumer;
import org.technamin.assignment.model.Information;
import org.technamin.assignment.model.Item;
import org.technamin.assignment.model.UpdateType;
import org.technamin.assignment.service.ItemsRepository;
import org.technamin.assignment.service.MongoItemService;
import org.technamin.assignment.service.RabbitMQService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    private static final Map<Integer, DocMetadata> map = new ConcurrentHashMap<>();
    private static final MongoItemService mongoService = new MongoItemService();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        final List<Item> items = ItemsRepository.INSTANCE.getItems();

        for (Item item : items) {
            final var docMetadata = map.computeIfAbsent(item.getDocId(), integer -> new DocMetadata());
            synchronized (docMetadata) {
                docMetadata.queue.add(item);
                if (!docMetadata.isRunning) {
                    docMetadata.isRunning = true;
                    EXECUTOR_SERVICE.submit(() -> processItemsOfDoc(docMetadata));
                }
            }
        }
        RabbitMQConsumer.defaultConsumerInit();
        EXECUTOR_SERVICE.shutdown();
    }

    private static void processItemsOfDoc(DocMetadata docMetadata) {
        while (true) {
            Item item;
            synchronized (docMetadata) {
                item = docMetadata.queue.poll();
                if (item == null) {
                    docMetadata.isRunning = false;
                    return;
                }
                saveOrUpdateItem(item);
            }
        }
    }

    private static void saveOrUpdateItem(Item item) {
        final Item byDocId = mongoService.findByDocId(item.getDocId());
        if (byDocId == null) {
            mongoService.save(item);
            RabbitMQService.sendLog(new Information(item.getDocId(), UpdateType.SAVE, item.getData()));
        } else {
            final var updateItemFields = mongoService.updateItemFields(item);
            RabbitMQService.sendLog(new Information(item.getDocId(), UpdateType.UPDATE, updateItemFields.toString()));
        }
    }

    private static class DocMetadata {
        private final Queue<Item> queue = new LinkedList<>();
        private boolean isRunning = false;
    }
}
