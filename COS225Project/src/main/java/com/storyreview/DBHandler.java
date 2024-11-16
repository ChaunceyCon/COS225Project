package com.storyreview;

import java.util.*;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class DBHandler {

    MongoClient client;
    MongoDatabase database;
    MongoCollection<Document> storiesCollection;

    public DBHandler() {
        // Use the new connection string here
        String conString = "mongodb+srv://chaunceyoconnell:SAxo0pQWMFYt43Rj@project225.vp8va.mongodb.net/?retryWrites=true&w=majority&appName=Project225";
        client = MongoClients.create(conString);
        
        // Change the database to the new one you created (e.g., "project225")
        database = client.getDatabase("project225");
        
        // Change the collection name if needed or keep "stories"
        storiesCollection = database.getCollection("stories");
    }

    public void addStory(String line) {
        Scanner parser = new Scanner(line);
        parser.useDelimiter(";;");
        try {
            String title = parser.next();
            String story = parser.next();
            String labels = parser.next();
            Document targetStory = new Document();
            targetStory.append("title", title).append("story", story).append("labels", labels);
            storiesCollection.insertOne(targetStory);
        } finally {
            parser.close();
        }
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public static void main(String[] args) {
        // Create an instance of DBHandler
        DBHandler dbHandler = new DBHandler();

        // Example of adding a story to the database
        String storyLine = "Title;;This is a story;;happy";
        dbHandler.addStory(storyLine);

        // Close the database connection
        dbHandler.close();
    }
}
