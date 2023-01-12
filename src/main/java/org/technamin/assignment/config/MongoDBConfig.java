package org.technamin.assignment.config;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.technamin.assignment.model.Item;


public enum MongoDBConfig {
    INSTANCE;

    private final Datastore datastore;

    MongoDBConfig() {
        String mongoHost = System.getProperty("MONGO_HOST", "localhost");
        int mongoPort = Integer.parseInt(System.getProperty("MONGO_PORT", "27017"));
        String database = System.getProperty("MONGO_DATABASE", "items");

        ServerAddress sa = new ServerAddress(mongoHost, mongoPort);
        Morphia morphia = new Morphia();
        morphia.map(Item.class);
        datastore = morphia.createDatastore(new MongoClient(sa), database);
        datastore.ensureIndexes();
    }

    public Datastore getDatastore() {
        return this.datastore;
    }
}
