package org.technamin.assignment.config;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.technamin.assignment.model.Item;


public enum MongoDBHelper {
    INSTANCE;

    private final Datastore datastore;

    private final String SERVER_URL = "localhost";
    private final String DATABASE_NAME = "items";
    private final int SERVER_PORT = 27017;
//    private final String USERNAME = "";
//    private final String PASSWORD = "";
//    private final String DOCKER_MONGO_HOST = "mongodb";
//    private final String DOCKER_MONGO_URI = "mongodb://mongodb/";

    MongoDBHelper() {
        ServerAddress sa = new ServerAddress(SERVER_URL, SERVER_PORT);
        Morphia morphia = new Morphia();
        morphia.map(Item.class);
        datastore = morphia.createDatastore(new MongoClient(sa), DATABASE_NAME);
        datastore.ensureIndexes();
    }

    public Datastore getDatastore() {
        return this.datastore;
    }
}
