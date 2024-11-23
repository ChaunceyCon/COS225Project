package com.storyreview.database;

import java.io.*;
import java.util.*;

import com.storyreview.story.*;

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

    //adds a story to Mongo from a Story object
    public void addStory(Story s) {
        String title = s.getTitle();
        String story = s.getFinStory();
        //recombine Story's emotion list into a single string
        ArrayList<String> emotions = s.getEmotions();
        String labels = "";
        for(int i=0;i<(emotions.size());i++) {
            labels+=emotions.get(i);
            if(!(i==emotions.size()-1)) {
                labels+="/";
            }
        }

        //add to Mongo
        Document targetStory = new Document();
        targetStory.append("title", title).append("story", story).append("labels", labels);
        storiesCollection.insertOne(targetStory);
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }
}

