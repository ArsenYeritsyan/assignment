package org.technamin.assignment.config;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.technamin.assignment.model.Item;


public enum MongoDBConfig {
    INSTANCE;
    //    private final String MONGO_HOST = "localhost";   -use without Docker
    private static final String DOCKER_SERVER_URL = "mongodb";
    private static final String DATABASE_NAME = "items";
    private static final int SERVER_PORT = 27017;
    private final Datastore datastore;


    MongoDBConfig() {
        ServerAddress sa = new ServerAddress(DOCKER_SERVER_URL, SERVER_PORT);
        Morphia morphia = new Morphia();
        morphia.map(Item.class);
        datastore = morphia.createDatastore(new MongoClient(sa), DATABASE_NAME);
        datastore.ensureIndexes();
    }

    public Datastore getDatastore() {
        return this.datastore;
    }
}
