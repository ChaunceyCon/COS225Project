package COS225Project.src.main.java.com.storyreview;

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
    
    public DBHandler(String conString) {
        // Use the provided connection string directly without redefining
        client = MongoClients.create(conString);
        database = client.getDatabase("admin");
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
}
