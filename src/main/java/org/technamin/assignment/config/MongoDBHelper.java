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
    private final int SERVER_PORT = 27017;
    private final String USERNAME = "";
    private final String PASSWORD = "";
    private final String DATABASE_NAME = "items";
    private final String DOCKER_MONGO_HOST = "mongodb";
    private final String DOCKER_MONGO_COLLECTION = "items";
    private final String DOCKER_MONGO_URI = "mongodb://mongodb/";

    MongoDBHelper() {
        ServerAddress sa = new ServerAddress(SERVER_URL, SERVER_PORT);
        Morphia morphia = new Morphia();
        morphia.map(Item.class);
        datastore = morphia.createDatastore(new MongoClient(sa), DATABASE_NAME);
        datastore.ensureIndexes();
//        datastore.ensureCaps();
    }


//        MongoClient mongo = new MongoClient(DOCKER_MONGO_URI);
////        MongoClient mongo = new MongoClient(DOCKER_MONGO_HOST, DOCKER_MONGO_PORT);
//        Morphia morphia = new Morphia();
//        datastore = morphia.createDatastore(mongo, DOCKER_MONGO_COLLECTION);
//        morphia.mapPackage("org.technamin.assignment");
//        datastore.ensureIndexes();

//        Document result = this.datastore.getMongo()
//                .getDatabase(DOCKER_MONGO_HOST)
//                .getCollection(DOCKER_MONGO_COLLECTION)
//                .find()
//                .first();
//
//        if (result != null) {
//            System.out.println("connected::.");
//        }

//        final Morphia morphia = new Morphia();
//        List<MongoCredential> credentials = new ArrayList<>();
//        credentials.add(MongoCredential.createCredential(USERNAME, DATABASE_NAME, PASSWORD.toCharArray()));
//        morphia.mapPackage("org.technamin.assignment");
//        this.datastore = morphia.createDatastore(new MongoClient(SERVER_URL, SERVER_PORT), DATABASE_NAME);
//        datastore.ensureIndexes();
//    }

    public Datastore getDatastore() {
        return this.datastore;
    }
}
