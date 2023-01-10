package org.technamin.assignment.service;

import com.mongodb.DuplicateKeyException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.technamin.assignment.config.MongoDBHelper;
import org.technamin.assignment.model.Item;

import java.math.BigInteger;
import java.util.ConcurrentModificationException;
import java.util.List;

public class MongoItemService {
    private final static String ID = "doc_id";
    private final Class<Item> entityClass = Item.class;
    private final Datastore datastore = MongoDBHelper.INSTANCE.getDatastore();

    public void save(Item entity) {
        try {
            datastore.save(entity);
        } catch (ConcurrentModificationException | DuplicateKeyException ex) {
            ex.printStackTrace();
        }
    }

    public void updateItemFieldById(int id, String fieldName, String fieldUpdate) {
        UpdateOperations<Item> updateOptions = datastore.createUpdateOperations(entityClass);
        Item item = find(ID,id).get(0);
        if (item != null) {
            updateOptions.set(fieldName, fieldUpdate);
        }
        datastore.update(datastore.createQuery(entityClass).field(ID).equal(id), updateOptions);
    }

    public void delete(Item entity) {
        datastore.delete(entity);
    }

    public Item findOne(final String key, final Object value) {
        return datastore.find(Item.class).filter(key, value).get();
    }

    public List<Item> find(String key, Object value) {
        return datastore.find(entityClass).filter(key, value).asList();
    }


    public Item findById(int id) {
        return datastore.get(entityClass, id);
    }


    public List<Item> findAll() {
        Query<Item> qr = datastore.find(entityClass);
        return qr.asList();
    }

    public long count() {
        return datastore.getCount(entityClass);
    }

    public Query<Item> createQuery() {
        return datastore.createQuery(entityClass);
    }

}
