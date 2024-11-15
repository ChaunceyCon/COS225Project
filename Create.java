package com.storyreview.review;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Create {

    public static void main(String[] args) {
        String connectionString = "mongodb+srv://<username>:<password>@<cluster-name>.vajhl.mongodb.net/?retryWrites=true&w=majority&appName=<cluster-name>";

        try (MongoClient mongoclient = MongoClients.create(connectionString)) {

            // Connect to the desired database
            MongoDatabase database = mongoclient.getDatabase("story_database");  // Change to your DB name

            // Connect to the stories collection (this collection will store the short stories)
            MongoCollection<Document> storiesCollection = database.getCollection("stories");

            // Creating example story documents
            Document firstStory = new Document();
            firstStory.append("story_text", "Once upon a time, there was a little girl who was very happy. She had a good family and friends. One day, she went to the park and played with her dog. She laughed and enjoyed her time. The day was perfect.")
                      .append("tone", "Optimistic");

            Document secondStory = new Document();
            secondStory.append("story_text", "A boy woke up to a rainy day. He had no friends and always felt lonely. He stayed inside, reading books to pass time. He wanted to go outside but feared the world. His life seemed dull.")
                       .append("tone", "Pessimistic");

            Document thirdStory = new Document();
            thirdStory.append("story_text", "There was a traveler who had a long journey ahead. He felt tired but kept walking. The road was tough, but he kept moving forward. Eventually, he saw a beautiful sunset. He felt proud of his progress.")
                       .append("tone", "Optimistic");

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
