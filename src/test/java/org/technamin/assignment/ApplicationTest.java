package org.technamin.assignment;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import org.junit.jupiter.api.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.technamin.assignment.model.Item;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


class ApplicationTest {
    private static final String MONGO_HOST = "localhost";
    private static final String DOCKER_MONGO_COLLECTION = "items";
    private static final int DOCKER_MONGO_PORT = 27017;

    @Test
    void testSave() throws Exception {
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
}