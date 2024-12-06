package com.storyreview.menu;

import com.storyreview.mlp.TFIDF;
import com.storyreview.mlp.Classifier;
import java.util.Scanner;

public class Menu {

    private TFIDF tfidf;
    private Classifier classifier;

    public Menu(TFIDF tfidf, Classifier classifier) {
        this.tfidf = tfidf;
        this.classifier = classifier;
    }

    public void runMenu() {
        try (Scanner scanner = new Scanner(System.in)) { // Ensure Scanner is closed automatically
            boolean exit = false;

            while (!exit) {
                // Display menu options
                System.out.println("Story Review Menu:");
                System.out.println("1. Classify a user story");
                System.out.println("2. Display TFIDF Information");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1-3): ");

                int choice;
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } else {
                    System.out.println("Invalid input. Please enter a number."); 
                    scanner.nextLine(); // Clear invalid input
                    continue;
                }

                switch (choice) {
                    case 1:
                        classifyUserStory(scanner);
                        break;
                    case 2:
                        displayTFIDFInfo();
                        break;
                    case 3:
                        System.out.println("Exiting the story review system...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select an option between 1 and 3.");
                }
            }
        }
    }

    private void classifyUserStory(Scanner scanner) {
        System.out.println("Enter the text of the story:");
        String userStory = scanner.nextLine();

        if (tfidf == null || classifier == null) {
            System.out.println("TFIDF or Classifier is not properly initialized.");
            return;
        }

        String sentiment = classifier.classifyUserStory(userStory, tfidf);
        System.out.println("The sentiment of the story is: " + sentiment);
    }

    private void displayTFIDFInfo() {
        if (tfidf == null) {
            System.out.println("TFIDF is not properly initialized.");
            return;
        }

        System.out.println("TFIDF information:");
        tfidf.displayInfo();
    }
}


        
        
        //run a while loop until the user chooses to exit
        //each iteration, display the menu options and ask the user for a number representing their choice
        //use a switch statement to use the appropriate feature based on what the user selects
        //for the main mlp feature you just need to get another user input for the user's story and then run classifier.classifyUserStory(String userStoryThatYouJustGot,TFIDFProcessor); and it should work


