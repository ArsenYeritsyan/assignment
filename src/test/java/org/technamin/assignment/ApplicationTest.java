package org.technamin.assignment;

import com.mongodb.DuplicateKeyException;
import org.junit.jupiter.api.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.UpdateOperations;
import org.technamin.assignment.config.MongoDBConfig;
import org.technamin.assignment.exceptions.MongoProcessException;
import org.technamin.assignment.model.*;
import org.technamin.assignment.service.MongoItemService;
import org.technamin.assignment.service.RabbitMQService;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationTest {
    private final Datastore datastore = MongoDBConfig.INSTANCE.getDatastore();

    @Test
    void shouldUpdateItemField() {
        sendItemToSave(new ItemSaveDto(466, 99L, "fake_data", "1672816948529"));
        String fieldChange = "changed_data";
        sendItemToUpdate(new ItemUpdateDto(466, "data", fieldChange));
        assertEquals(find("doc_id", 466).get(0).getData(), fieldChange);
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