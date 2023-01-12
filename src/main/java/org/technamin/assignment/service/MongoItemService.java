package org.technamin.assignment.service;

import com.mongodb.DuplicateKeyException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.technamin.assignment.config.MongoDBConfig;
import org.technamin.assignment.exceptions.MongoProcessException;
import org.technamin.assignment.model.Item;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoItemService {
    private static final Logger logger = Logger.getLogger(MongoItemService.class.toString());
    private static final String UPDATED = "Updated ::: ";
    private static final String ID = "doc_id";
    private static final String SEQ = "seq";
    private static final String DATA = "data";
    private static final String TIME = "time";
    private final Datastore datastore = MongoDBConfig.INSTANCE.getDatastore();

    public void save(Item entity) {
        try {
            datastore.save(entity);
        } catch (ConcurrentModificationException | DuplicateKeyException ex) {
            throw new MongoProcessException(ex);
        }
    }

    public Map<String, Object> updateItemFields(Item entity) {
        final Map<String, Object> sequential = Map.of(SEQ, entity.getSeq(), DATA, entity.getData(), TIME, entity.getTime());
        updateItemFieldsById(entity.getDocId(), sequential);
        return sequential;
    }

    private void updateItemFieldsById(int id, Map<String, Object> fields) {
        UpdateOperations<Item> updateOptions = datastore.createUpdateOperations(Item.class);
        fields.forEach(updateOptions::set);
        datastore.update(createQuery().field(ID).equal(id), updateOptions);
        logger.log(Level.INFO, UPDATED, id);
    }

    public Item findOne(final String key, final Object value) {
        return datastore.find(Item.class).filter(key, value).get();
    }

    public void updateItemFieldById(int id, String fieldName, String fieldUpdate) {
        UpdateOperations<Item> updateOptions = datastore.createUpdateOperations(Item.class);
        Item item = findOne(ID, id);
        if (item != null) {
            updateOptions.set(fieldName, fieldUpdate);
        }
        datastore.update(createQuery().field(ID).equal(id), updateOptions);
        logger.log(Level.INFO, UPDATED, id);
    }

    public List<Item> find(String key, Object value) {
        return datastore.find(Item.class).filter(key, value).asList();
    }

    public Item findByDocId(int id) {
        return findOne(ID, id);
    }

    public void delete(Item entity) {
        datastore.delete(entity);
    }

    public List<Item> findAll() {
        Query<Item> qr = datastore.find(Item.class);
        return qr.asList();
    }

    public long count() {
        return datastore.getCount(Item.class);
    }

    public Query<Item> createQuery() {
        return datastore.createQuery(Item.class);
    }

}
