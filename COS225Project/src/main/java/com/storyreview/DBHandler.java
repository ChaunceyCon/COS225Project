package com.storyreview;

import java.io.*;
import java.util.*;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBHandler {

    MongoClient client;
    MongoDatabase database;
    MongoCollection<Document> storiesCollection;

    public DBHandler() {
        // Use your MongoDB connection string here
        String conString = "mongodb+srv://chaunceyoconnell:SAxo0pQWMFYt43Rj@project225.vp8va.mongodb.net/?retryWrites=true&w=majority&appName=Project225";
        client = MongoClients.create(conString);
        database = client.getDatabase("project225");
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

    public void uploadFromFile(String filePath) {
        System.out.println("Reading from file: " + filePath);  // Debug print to verify file path
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Add each line as a story to the database
                addStory(line);
            }
            System.out.println("Successfully uploaded data from " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + filePath);
            e.printStackTrace();
        }
    }
    

    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public static void main(String[] args) {
        // Create DB Handler
        DBHandler dbHandler = new DBHandler();
        
        // Upload data from 'final.txt' and 'raw.txt'
        dbHandler.uploadFromFile("/mnt/c/Users/Chauncey/OneDrive - University of Maine System/Desktop/Java/COS225PROJECT/COS225PROJECT/COS225Project/src/main/resources/final.txt");
        dbHandler.uploadFromFile("/mnt/c/Users/Chauncey/OneDrive - University of Maine System/Desktop/Java/COS225PROJECT/COS225PROJECT/COS225Project/src/main/resources/raw.txt");

        // Close the connection
        dbHandler.close();
    }
}

