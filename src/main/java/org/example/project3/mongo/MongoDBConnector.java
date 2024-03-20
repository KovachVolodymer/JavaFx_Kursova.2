package org.example.project3.mongo;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBConnector {
    private MongoDatabase database;

    public MongoDBConnector() {
        try {
            // Створення підключення до MongoDB
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            // Вибір бази даних
            database = mongoClient.getDatabase("kursova");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public boolean existsByName(String name) {
        return database.getCollection("user").find(new Document("login", name)).first() != null;
    }
}
