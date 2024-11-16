package COS225Project.src.main.java.com.storyreview;

import java.util.*;
import java.io.*;

public class Preprocessor {
    
    //removes all but the longest version of each story from the raw data
    public static void keepLongest(String readPath, String writePath) {
        String curTitle = "";
        String newTitle = "";
        String longLine = "";
        String curLine = "";
    
        try (
            Scanner lineScanner = new Scanner(new File(readPath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(writePath, true))
        ) {
            while (lineScanner.hasNextLine()) {
                curLine = lineScanner.nextLine();
                Scanner titleScanner = new Scanner(curLine);
                titleScanner.useDelimiter("/+");
                newTitle = titleScanner.next();
    
                // If the current title is different from the previous line, add our current longest line to the write file and then look for a new one with the new title
                if (!curTitle.equals(newTitle)) {
                    if (!curTitle.isEmpty()) {
                        writer.write(longLine + "\n");
                    }
                    longLine = "";
                    curTitle = newTitle;
                    System.out.println("Starting the section " + curTitle);
                }
    
                // Update the longest line if the current line is longer, also prevents the first empty longline
                if (curLine.length() > longLine.length()) {
                    longLine = curLine;
                }
                titleScanner.close();
            }
    
            // Write the last longest line after finishing the loop
            if (!longLine.isEmpty()) {
                writer.write(longLine + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error accessing the file!");
            e.printStackTrace();
        }
    }
    

    //removes punctuation, decapitalizes the contents of the story, and separates the emotional labels from the story in a more easily parsable way
    public static void decapitalize(String readPath,String writePath)
    {
        String title;
        String iniStory="";
        String finStory="";
        String labels="";
        String currLine = "";
        try
        {
            File readFile = new File(readPath);
            Scanner lineScanner = new Scanner(readFile);
            while(lineScanner.hasNextLine())
            {
                currLine=lineScanner.nextLine();
                Scanner storyScanner = new Scanner(currLine);
                storyScanner.useDelimiter("/");
                title=storyScanner.next();
                storyScanner.useDelimiter("[\\.|!|?]\\/\"\\[\"\"");
                iniStory=storyScanner.next();
                storyScanner.useDelimiter("\\W+");
                while(storyScanner.hasNext())
                {
                    labels+=storyScanner.next();
                    if(storyScanner.hasNext())
                    {labels+="/";}
                }
                storyScanner.close();
                Scanner wordScanner = new Scanner(iniStory);
                wordScanner.useDelimiter("\\W+");
                while(wordScanner.hasNext())
                {
                    finStory+=wordScanner.next().toLowerCase();
                    if(!wordScanner.hasNext())
                    {
                        finStory+=";;";
                    }
                    else
                    {
                        finStory+=" ";
                    }
                }
                wordScanner.close();
                try(FileWriter writer = new FileWriter(writePath,true))
                {
                    writer.write(title+";;"+finStory+labels+"\n");
                }
                catch(IOException e)
                {
                    System.out.println("There was an error finding the file to write to!");
                    e.printStackTrace();
                }
                labels="";
                finStory="";
                System.out.println("Finished the section "+title);
            }
            lineScanner.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Error finding the file to read!");
            e.printStackTrace();
        }
    }
}
