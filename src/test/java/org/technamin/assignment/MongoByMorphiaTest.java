package org.technamin.assignment;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.UpdateOperations;
import org.technamin.assignment.exceptions.MongoProcessException;
import org.technamin.assignment.model.Information;
import org.technamin.assignment.model.Item;
import org.technamin.assignment.model.UpdateType;
import org.technamin.assignment.service.RabbitMQService;

import java.util.ConcurrentModificationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class MongoByMorphiaTest {
    private static final String MONGO_HOST = "localhost";
    private static final String DOCKER_MONGO_COLLECTION = "items";
    private static final int DOCKER_MONGO_PORT = 27017;
    private static final Datastore datastore;

    static {
        MongoClient mongo = new MongoClient(MONGO_HOST, DOCKER_MONGO_PORT);
        Morphia morphia = new Morphia();
        datastore = morphia.createDatastore(mongo, DOCKER_MONGO_COLLECTION);
    }


    @Test
    @Order(1)
    void shouldUpdateItemField() {
        sendItemToSave(new ItemSaveDto(466, 99L, "fake_data", "1672816948529"));
        String fieldChange = "changed_data";
        sendItemToUpdate(new ItemUpdateDto(466, "data", fieldChange));
        assertEquals(find("doc_id", 466).get(0).getData(), fieldChange);
    }

    @Test
    void shouldReturnNonNullResult() {
        MongoClient mongo = new MongoClient(MONGO_HOST, DOCKER_MONGO_PORT);
        Morphia morphia = new Morphia();
        Datastore ds = morphia.createDatastore(mongo, DOCKER_MONGO_COLLECTION);
        try {
            FindIterable<Item> result = ds.getMongo()
                    .getDatabase(MONGO_HOST)
                    .getCollection(DOCKER_MONGO_COLLECTION)
                    .withDocumentClass(Item.class)
                    .find();
            assertNotEquals(null, result);
        } finally {
            ds.getMongo().close();
        }
    }

    public void sendItemToSave(ItemSaveDto saveDto) {
        Item toSave = new Item(saveDto.getId(), saveDto.getSeq(), saveDto.getData(), saveDto.getTime());
        save(toSave);
        Information information = new Information(saveDto.getId(), UpdateType.SAVE, toSave.getData());
        RabbitMQService.sendLog(information);
    }

    public void sendItemToUpdate(ItemUpdateDto updateDto) {
        updateItemFieldById(updateDto.getDocId(), updateDto.getFieldName(), updateDto.getFieldUpdateValue());
        Information information = new Information(updateDto.getDocId(), UpdateType.UPDATE,
                updateDto.getFieldName(), updateDto.getFieldUpdateValue());
        RabbitMQService.sendLog(information);
    }

    public void save(Item entity) {
        try {
            datastore.save(entity);
        } catch (ConcurrentModificationException | DuplicateKeyException ex) {
            throw new MongoProcessException(ex);
        }
    }

    public void updateItemFieldById(int id, String fieldName, String fieldUpdate) {
        UpdateOperations<Item> updateOptions = datastore.createUpdateOperations(Item.class);
        String ID = "doc_id";
        Item item = find(ID, id).get(0);
        if (item != null) {
            updateOptions.set(fieldName, fieldUpdate);
        }
        datastore.update(datastore.createQuery(Item.class).field(ID).equal(id), updateOptions);
    }

    public List<Item> find(String key, Object value) {
        return datastore.find(Item.class).filter(key, value).asList();
    }

}