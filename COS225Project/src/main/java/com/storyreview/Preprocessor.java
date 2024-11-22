package com.storyreview;

import java.util.*;
import java.io.*;

public class Preprocessor {
    
    //removes all but the longest version of each story from the raw data
    public static void keepLongest(String readPath, String writePath) {
        String curTitle = "";
        String newTitle = "";
        String longLine = "";
        String curLine = "";
    
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

        } catch (IOException e) {
            System.out.println("Error accessing the file!");
            e.printStackTrace();
        }
    }

    //removes punctuation, decapitalizes the contents of the story, and separates the emotional labels from the story in a more easily parsable way
    public static void decapitalize(String readPath, String writePath) {
        String title = "";
        String iniStory = "";
        String finStory = "";
        String labels = "";
        String currLine = "";
    
        try (Scanner lineScanner = new Scanner(new File(readPath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(writePath, true))) {
    
            while (lineScanner.hasNextLine()) {
                currLine = lineScanner.nextLine();
                Scanner storyScanner = new Scanner(currLine);
                storyScanner.useDelimiter(";;");
    
                // Extract title, story, and labels 
                title = storyScanner.next();
                iniStory = storyScanner.next();
                labels = storyScanner.next();
                
                // Remove punctuation and decapitalize the story content
                Scanner wordScanner = new Scanner(iniStory);
                wordScanner.useDelimiter("^[\\w|']+");
                finStory = "";
                while (wordScanner.hasNext()) {
                    finStory += wordScanner.next().toLowerCase();
                    if (wordScanner.hasNext()) {
                        finStory += " ";
                    } else {
                        finStory += ";;";
                    }
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
                writer.write(title + ";;" + finStory + labels + "\n");
                System.out.println("Processed section: " + title);

                storyScanner.close();
            }

        } catch (IOException e) {
            System.out.println("Error accessing the file!");
            e.printStackTrace();
        }
    }
}
