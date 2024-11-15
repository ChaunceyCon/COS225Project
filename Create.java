package com.storyreview.review;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Create {

    public static void main(String[] args) {
        String connectionString = // Connection string;

        try (MongoClient mongoclient = MongoClients.create(connectionString)) {

            
            MongoDatabase database = mongoclient.getDatabase("story_database");  

            MongoCollection<Document> storiesCollection = database.getCollection("stories");


            // Inserting stories into the MongoDB collection
            storiesCollection.insertOne(firstStory);
            storiesCollection.insertOne(secondStory);
            storiesCollection.insertOne(thirdStory);

            System.out.println("Stories have been successfully inserted into the database!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
