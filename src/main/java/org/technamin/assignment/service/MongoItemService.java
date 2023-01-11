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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoItemService {
    private static final Logger logger = Logger.getLogger(MongoItemService.class.toString());
    private static final String UPDATED = "Updated ::: ";
    private static final String ID = "doc_id";
    private final Datastore datastore = MongoDBConfig.INSTANCE.getDatastore();

    public void save(Item entity) {
        try {
            datastore.save(entity);
        } catch (ConcurrentModificationException | DuplicateKeyException ex) {
            throw new MongoProcessException(ex);
        }
    }

    public void updateItemFieldById(int id, String fieldName, String fieldUpdate) {
        UpdateOperations<Item> updateOptions = datastore.createUpdateOperations(Item.class);
        Item item = find(ID, id).get(0);
        if (item != null) {
            updateOptions.set(fieldName, fieldUpdate);
        }
        datastore.update(datastore.createQuery(Item.class).field(ID).equal(id), updateOptions);
        logger.log(Level.INFO, UPDATED, id);
    }

    public void delete(Item entity) {
        datastore.delete(entity);
    }

    public Item findOne(final String key, final Object value) {
        return datastore.find(Item.class).filter(key, value).get();
    }

    public List<Item> find(String key, Object value) {
        return datastore.find(Item.class).filter(key, value).asList();
    }

    public Item findById(int id) {
        return datastore.get(Item.class, id);
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
