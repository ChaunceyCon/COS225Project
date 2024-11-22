package com.storyreview.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.storyreview.DBHandler;

public class Startup {
    

    public static void main(String[] args) {
        try {
            // Get connection string from file and create DB Handler
            Scanner csScanner = new Scanner(new File("src/resources/conString.txt"));
            String conString = csScanner.nextLine();
            DBHandler dbHandler = new DBHandler(conString,"stories");
            csScanner.close();

            // Upload data from final.txt
            dbHandler.uploadFromFile("src/resources/final.txt");
            
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
