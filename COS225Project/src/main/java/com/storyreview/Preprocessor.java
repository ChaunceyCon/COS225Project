package com.storyreview;

import java.util.*;
import java.io.*;

import com.storyreview.mlp.*;

public class Preprocessor {
    
    //total number of stories (documents) in the dataset
    private static int storyCount;
    private static ArrayList<String> stopWords;
    
    //removes all but the longest version of each story from the raw data
    public static void keepLongest(String readPath, String writePath) {
        String curTitle = "";
        String newTitle = "";
        String longLine = "";
        String curLine = "";
        
        System.out.println("Now beginning keepLongest");
        try (Scanner lineScanner = new Scanner(new File(readPath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(writePath, true))) {
    
            while (lineScanner.hasNextLine()) {
                curLine = lineScanner.nextLine();
                Scanner titleScanner = new Scanner(curLine);
                titleScanner.useDelimiter("/+");
                newTitle = titleScanner.next();

                // line has a different title than the previous line, add our current longest to the write file and then look for a new one with a new title
                if (!curTitle.equals(newTitle)) {
                    if (!curTitle.equals("")) {
                        writer.write(longLine + "\n");
                    }
                    longLine = "";
                    curTitle = newTitle;
                    System.out.println("Starting the section " + curTitle);
                }

                // Update the longest line for the current title
                if (curLine.length() > longLine.length()) {
                    longLine = curLine;
                }

                titleScanner.close();
            }

            // Write the longest line for the last title
            if (!longLine.isEmpty()) {
                writer.write(longLine + "\n");
            }

            System.out.println("keepLongest completed!");
            System.out.println();

        } catch (IOException e) {
            System.out.println("Error accessing the file!");
            e.printStackTrace();
        }
    }

    //fills stopWords based on the stopWords.txt file
    public static void fillStopWords() {
        try(Scanner swScanner = new Scanner(new File("src/resources/conString.txt"))) {
            while(swScanner.hasNextLine()) {
                stopWords.add(swScanner.nextLine());
            }
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error finding the file of stop-words!");
        }
    }

    //removes punctuation, decapitalizes the contents of the story, and separates the emotional labels from the story in a more easily parsable way
    public static void processFile(String readPath, String writePath) {
        storyCount=0;
        //initialize all the random variables we'll need for processing
        String title = "";
        String iniStory = "";
        String finStory = "";
        String labels = "";
        String currLine = "";
        String word;
        //create the stopWords list
        fillStopWords();
        //create the TFIDF object to effeciently get the necessary MLP data during processing
        TFIDF storyProcessor = new TFIDF();
        
        System.out.println("Starting processFile");
    
        try (Scanner lineScanner = new Scanner(new File(readPath));
        BufferedWriter writer = new BufferedWriter(new FileWriter(writePath, true))) {

            //extract and process a story and associated data from each line of the specified file
            while (lineScanner.hasNextLine()) {
                currLine = lineScanner.nextLine();
                Scanner storyScanner = new Scanner(currLine);
                storyScanner.useDelimiter(";;");
    
                // Extract title, unprocessed story, and labels 
                title = storyScanner.next();
                iniStory = storyScanner.next();
                labels = storyScanner.next();
                
                // Process the story by removing punctuation and stop-words and decapitalizing
                Scanner wordScanner = new Scanner(iniStory);
                wordScanner.useDelimiter("^[\\w|']+");
                finStory = "";
                //boolean representing if the current output of wordScanner.next() would be the first word in the story
                boolean first=true;
                while (wordScanner.hasNext()) {
                    //decapitalizes the next word
                    word=wordScanner.next().toLowerCase();
                    //adds the word if it isn't a stop-word
                    if(!stopWords.contains(word)) {
                        //adds a space before the word unless it's the first word of the story
                        if(!first) {
                            finStory += " ";
                        }
                        finStory += word;
                    }
                    first=false;
                }
                wordScanner.close();
    
                // Normalize labels by removing extra spaces
                Scanner labelScanner = new Scanner(labels);
                labelScanner.useDelimiter("/");
                labels = "";
                while (labelScanner.hasNext()) {
                    labels += labelScanner.next().trim();
                    if (labelScanner.hasNext()) {
                        labels += "/";
                    }
                }
                labelScanner.close();
    
                // Write formatted data to the output file
                writer.write(title + ";;" + finStory + ";;" + labels + "\n");
                //increment storyCount for each story successfully added
                storyCount++;
                //extract the necessary TFIDF information
                storyProcessor.processStory(finStory,title,storyCount);
                System.out.println("Processed section: " + title);

                storyScanner.close();
            }
            //once all stories have been processed, fill storyProcessor's IDFHash
            storyProcessor.fillIDFHash(storyCount);

            System.out.println("Finished processFile!");

        } catch (IOException e) {
            System.out.println("Error accessing the file!");
            e.printStackTrace();
        }
    }

    //get method for storyCount
    public static int getStoryCount() {
        return storyCount;
    }
}
