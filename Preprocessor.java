import java.util.*;
import java.io.*;

public class Preprocessor {
    
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
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Couldn't find the file to read!");
            e.printStackTrace();
        }

    }

    public static void decapitalize(String readFile,String writeFile)
    {

    }

    public static void main(String[] args)
    {
        Preprocessor.keepLongest("raw.txt","longest.txt");
    }

}
