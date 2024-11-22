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

    public DBHandler(String conString,String colName) {
        client = MongoClients.create(conString);
        database = client.getDatabase("project225");
        storiesCollection = database.getCollection(colName);
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
        try {
            Scanner lineScanner = new Scanner(new File(filePath));
            String line;
            while (lineScanner.hasNextLine()) {
                // Add each line as a story to the database
                line=lineScanner.nextLine();
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
        try {
            // Get connection string from file and create DB Handler
            Scanner csScanner = new Scanner(new File("src/resources/conString.txt"));
            String conString = csScanner.nextLine();
            DBHandler dbHandler = new DBHandler(conString,"stories");
            csScanner.close();

            // Upload data from 'final.txt' and 'raw.txt'
            //dbHandler.uploadFromFile("/mnt/c/Users/Chauncey/OneDrive - University of Maine System/Desktop/Java/COS225PROJECT/COS225PROJECT/COS225Project/src/main/resources/final.txt");
            //dbHandler.uploadFromFile("/mnt/c/Users/Chauncey/OneDrive - University of Maine System/Desktop/Java/COS225PROJECT/COS225PROJECT/COS225Project/src/main/resources/raw.txt");
            dbHandler.uploadFromFile("src/resources/final.txt");
            dbHandler.uploadFromFile("src/resources/raw.txt");
            
            // Close the connection
            dbHandler.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Error finding the connection string file!");
        }
    }
}

