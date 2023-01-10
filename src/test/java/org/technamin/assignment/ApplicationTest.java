package org.technamin.assignment;

import com.mongodb.MongoClient;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


class ApplicationTest {
    private static final String DOCKER_MONGO_HOST = "mongodb";
    private static final String DOCKER_MONGO_COLLECTION = "items";
    private static final int DOCKER_MONGO_PORT = 27017;

    @Test
    public void testSave() throws Exception {
        MongoClient mongo = new MongoClient(DOCKER_MONGO_HOST, DOCKER_MONGO_PORT);
        Morphia morphia = new Morphia();
        Datastore ds = morphia.createDatastore(mongo, DOCKER_MONGO_COLLECTION);
        try {
            Document result = ds.getMongo()
                    .getDatabase(DOCKER_MONGO_HOST)
                    .getCollection(DOCKER_MONGO_COLLECTION).find().first();
            assertNotEquals(null, result);
            System.out.println("TEST PASSED.");
        } finally {
            ds.getMongo().close();
        }
    }
}