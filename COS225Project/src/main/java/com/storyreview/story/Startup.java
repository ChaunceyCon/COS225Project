package com.storyreview.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import com.storyreview.database.*;
//import com.storyreview.menu.*;
import com.storyreview.mlp.TFIDF;

public class Startup {
    

    public static void main(String[] args) {
        //vars for the paths of the final and intermediate files involved in preprocessing
        String rawPath = "src/resources/raw.txt";
        String shortPath = "src/resources/short.txt";
        String finPath = "src/resources/final.txt";
        
        //reset everything to defaults after last run. will probably move this to the end before the actual presentation
        Shutdown.reset(shortPath,finPath);

        //create a TFIDF object to effeciently get the necessary MLP data during processing
        TFIDF storyProcessor = new TFIDF();
        //fill final.txt by processing raw.txt with Preprocesser
        Preprocessor.keepLongest(rawPath,shortPath);
        Preprocessor.processFile(shortPath,finPath,storyProcessor);
        HashMap<String,Story> storyCollection = Preprocessor.getStoryCollection();
        
        try {
            // Get connection string from file and create DB Handler
            Scanner csScanner = new Scanner(new File("src/resources/conString.txt"));
            String conString = csScanner.nextLine();
            DBHandler dbHandler = new DBHandler(conString,"stories");
            csScanner.close();

            // Upload data from storyCollection
            int storiesAdded=0;
            for(Story s : storyCollection.values()) {
                dbHandler.addStory(s);
                storiesAdded++;
            }
            System.out.println("Added "+storiesAdded+" stories to MongoDB");
            
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
