package org.technamin.assignment;

import org.technamin.assignment.config.ItemsRepository;
import org.technamin.assignment.config.RabbitMQConsumer;
import org.technamin.assignment.model.Information;
import org.technamin.assignment.model.Item;
import org.technamin.assignment.model.UpdateType;
import org.technamin.assignment.service.MongoItemService;
import org.technamin.assignment.service.RabbitMQService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Application {
    private static final Map<Integer, DocMetadata> map = new ConcurrentHashMap<>();
    private static final MongoItemService mongoService = new MongoItemService();

    public static void main(String[] args) {

        final var items = ItemsRepository.INSTANCE.getItems();

        items.stream()
                .parallel()
                .forEach((Item item) -> {
                    final var docMetadata = map.computeIfAbsent(item.getDocId(), integer -> new DocMetadata());
                    final var seq = item.getSeq();
                    if (docMetadata.currentSeq.get() == seq) {
                        Item currentItem = item;
                        do {
                            processItem(currentItem);
                            currentItem = docMetadata.availableItems.remove(docMetadata.currentSeq.incrementAndGet());
                        } while (currentItem != null);
                    } else {
                        docMetadata.availableItems.put(seq, item);
                    }
                });

        RabbitMQConsumer.defaultConsumerInit();
    }

    private static void processItem(Item item) {
        RabbitMQService.sendLog(new Information(item.getDocId(), UpdateType.SAVE, item.getData()));
        mongoService.save(item);
    }

    private static class DocMetadata {

        private final AtomicLong currentSeq = new AtomicLong();
        private final Map<Long, Item> availableItems = new ConcurrentHashMap<>();
    }

}
