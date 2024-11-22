package com.storyreview.story;

import java.io.*;

public class Shutdown {
    
    public static void reset(String shortPath,String finPath) {
        File shortest = new File(shortPath);
        File fin = new File(finPath);
        //clears the file Preprocessor.keepLongest() writes to by deleting and re-creating it
        shortest.delete();
        try {
            shortest.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error clearing "+shortPath);
        }
        //clears the file Preprocessor.processFile() writes to
        fin.delete();
        try {
            fin.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error clearing "+finPath);
        }
    }
}
