import java.util.*;
import java.io.*;

public class Preprocessor {
    
    //removes all but the longest version of each story from the raw data
    public static void keepLongest(String readPath,String writePath)
    {
        String curTitle="";
        String newTitle="";
        String longLine="";
        String curLine="";
        try
        {
            File readFile = new File(readPath);
            Scanner lineScanner = new Scanner(readFile);
            while(lineScanner.hasNextLine())
            {
                curLine=lineScanner.nextLine();
                Scanner titleScanner = new Scanner(curLine);
                titleScanner.useDelimiter("/+");
                newTitle=titleScanner.next();
                //if our current line has a different title than the previous line, add our current longest line to the write file and then look for a new one with the new title
                if(!curTitle.equals(newTitle))
                {
                    //this if statement prevents the first empty longLine
                    if(!curTitle.equals(""))
                    {
                        try(FileWriter writer = new FileWriter(writePath,true))
                        {
                            writer.write(longLine+"\n");
                        }
                        catch(IOException e)
                        {
                            System.out.println("Error finding the file to write!");
                            e.printStackTrace();
                        }
                    }
                    longLine="";
                    curTitle=newTitle;
                    System.out.println("Starting the section "+curTitle);
                }
                if(curLine.length()>longLine.length())
                {longLine=curLine;}
                titleScanner.close();
            }
            lineScanner.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Couldn't find the file to read!");
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
                storyScanner.useDelimiter("\\.\\/\"\\[\"\"");
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

    public static void main(String[] args)
    {
        //done: Preprocessor.keepLongest("raw.txt","longest.txt");
        Preprocessor.decapitalize("longest.txt", "final.txt");

    }

}
