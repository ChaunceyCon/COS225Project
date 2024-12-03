package com.storyreview.menu;

import com.storyreview.mlp.TFIDF;
import com.storyreview.mlp.Classifier;
import com.storyreview.story.Story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {

    private TFIDF tfidf;
    private Classifier classifier;

    public Menu(TFIDF tfidf, Classifier classifier) {
        this.tfidf = tfidf;
        this.classifier = classifier;
    }

    public void runMenu() {
        try (Scanner scanner = new Scanner(System.in)) { 
            boolean exit = false;

            while (!exit) {
                // Display menu options for user
                System.out.println("Story Review Menu:");
                System.out.println("1. Classify a user story");
                System.out.println("2. Display TFIDF Information");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1-3): ");

    }
}
    }
}
        
        
        //run a while loop until the user chooses to exit
        //each iteration, display the menu options and ask the user for a number representing their choice
        //use a switch statement to use the appropriate feature based on what the user selects
        //for the main mlp feature you just need to get another user input for the user's story and then run classifier.classifyUserStory(String userStoryThatYouJustGot,TFIDFProcessor); and it should work


