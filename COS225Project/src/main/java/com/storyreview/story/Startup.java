package com.storyreview.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import com.storyreview.database.*;
import com.storyreview.menu.*;
import com.storyreview.mlp.*;

public class Startup {
    

    public static void main(String[] args) {
        //vars for the paths of the final and intermediate files involved in preprocessing
        String rawPath = "src/resources/raw.txt";
        String shortPath = "src/resources/short.txt";
        String finPath = "src/resources/final.txt";

        //create TFIDF and Classifier objects to effeciently get the necessary MLP data during processing
        TFIDF storyProcessor = new TFIDF();
        Classifier sorter = new Classifier();
        //fill final.txt by processing raw.txt with Preprocesser
        Preprocessor.keepLongest(rawPath,shortPath);
        Preprocessor.processFile(shortPath,finPath,storyProcessor,sorter);
        HashMap<String,Story> storyCollection = Preprocessor.getStoryCollection();

        //testing MLP stuff
        //String uStory = "For Christmas, George wanted a new toy. To his dismay, it was just a pair of socks. George was very angry. What a terrible present! This was the worst Christmas ever.";
        /*String uStory = "Tony was starting school tomorrow. He was very excited. He made a lot of new friends. All of his teachers were nice. He learned so much, and couldn't wait to go back again.";
        System.out.println("\n\nYour story is: \n"+uStory);
        System.out.println("Overall, this seems like a "+sorter.classifyUserStory(uStory, storyProcessor)+" story.");*/
        
        //adds all the Storys in storyCollection to the specified MongoDB database
        //you should probably usually comment this part out when just testing stuff
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
                System.out.println(s.getKey()+" successfully added");
                storiesAdded++;
                //code for the live demo to stop adding to mongo once we hit 2000
                if(storiesAdded==2000) {
                    break;
                }
            }
            System.out.println("Added "+storiesAdded+" stories to MongoDB");
            
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Error finding the connection string file!");
        }

        //create the Menu object and run runMenu()
        Menu m = new Menu(storyProcessor,sorter);
        m.runMenu();

        //reset everything to defaults after last run
        Shutdown.reset(shortPath,finPath);
        
    }
}
