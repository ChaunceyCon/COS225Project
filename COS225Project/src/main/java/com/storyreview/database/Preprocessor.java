package com.storyreview.database;

import java.util.*;
import java.io.*;

import com.storyreview.mlp.*;
import com.storyreview.story.*;

public class Preprocessor {
    
    //total number of stories (documents) in the dataset
    private static HashMap<String,Story> storyCollection = new HashMap<String,Story>();
    private static int storyCount;
    private static ArrayList<String> stopWords = new ArrayList<String>();
    
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

                //if line has a different title than the previous line, add our current longest to the write file and then look for a new one with a new title
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
        try(Scanner swScanner = new Scanner(new File("src/resources/stopWords.txt"))) {
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
    public static void processFile(String readPath, String writePath, TFIDF TFIDFProcessor) {
        storyCount=0;
        //initialize all the random variables we'll need for processing
        String title = "";
        String iniStory = "";
        String finStory = "";
        String labels = "";
        String currLine = "";
        String word;
        String emotion;
        //create the stopWords list
        fillStopWords();
        
        System.out.println("Starting processFile");
    
        try (Scanner lineScanner = new Scanner(new File(readPath))) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(writePath, true));
            
            //extract and process a story and associated data from each line of the specified file
            while (lineScanner.hasNextLine()) {
                currLine = lineScanner.nextLine();
                Scanner storyScanner = new Scanner(currLine);
                storyScanner.useDelimiter("/+");
    
                // Extract title, unprocessed story, and labels 
                title = storyScanner.next();
                //this needs 2 .next()s bc there's always a / before the beginning of the story's final sentence
                iniStory = storyScanner.next()+storyScanner.next();
                labels = storyScanner.next();
                
                // Process the story by removing punctuation and stop-words and decapitalizing
                Scanner wordScanner = new Scanner(iniStory);
                //matches any number of characters that are neither a letter or a '
                wordScanner.useDelimiter("[^[\\w|']]+");
                finStory = "";
                //boolean representing if the current output of wordScanner.next() would be the first word in the story
                boolean first=true;
                while (wordScanner.hasNext()) {
                    //decapitalizes the next word
                    word=wordScanner.next().toLowerCase();
                    //adds the word if it isn't a stop-word
                    if(!(stopWords.contains(word))) {
                        //adds a space before the word unless it's the first word of the story
                        if(!first) {
                            finStory += " ";
                        }
                        finStory += word;
                    }
                    first=false;
                }
                wordScanner.close();
    
                //Formats labels to be more parsable
                labels=labels.substring(4);
                Scanner labelScanner = new Scanner(labels);
                labelScanner.useDelimiter("(\\\"\\\",\\s\\\"\\\")|(\\\"\\\"\\]\\\")");
                labels = "";
                while (labelScanner.hasNext()) {
                    emotion=labelScanner.next().trim();
                    if(!(emotion.contains(" "))) {
                        labels += emotion;
                        if (labelScanner.hasNext()) {
                            labels += "/";
                        }
                    }
                }
                labelScanner.close();
    
                // Write formatted data to the output file
                writer.write(title + ";;" + finStory + ";;" + labels + "\n");
                //increment storyCount for each story successfully added
                storyCount++;
                //create a Story object from the current story and add it to storyCollection. the key for the story is the same as the key for it's TF values within TFHash
                Story s = new Story(title,iniStory,finStory,labels,storyCount);
                storyCollection.put(""+storyCount+"-"+title,s);
                //extract the necessary TFIDF information
                TFIDFProcessor.processStory(s,storyCount);
                System.out.println("Processed section: " + title);

                storyScanner.close();
            }
            //once all stories have been processed, fill TFIDFProcessor's IDFHash
            TFIDFProcessor.fillIDFHash(storyCount);

            writer.close();
            System.out.println("Finished processFile!");


        } catch (IOException e) {
            System.out.println("Error accessing the file!");
            e.printStackTrace();
        }
    }

    //get methods
    public static int getStoryCount() {
        return storyCount;
    }

    public static HashMap<String,Story> getStoryCollection() {
        return storyCollection;
    }

    //fills the specified .txt with all of the emotional labels used on the stories. should only need to be ran once
    public static void getLabelList(String writePath) {
        HashSet<String> emotions = new HashSet<String>();
        for(Story s : storyCollection.values()) {
            for(String e : s.getEmotions()) {
                emotions.add(e);
            }
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(writePath,true))) {
            for(String e : emotions) {
                writer.write(e+";;\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to "+writePath+" for getLabelList!");
        }
    }
}
