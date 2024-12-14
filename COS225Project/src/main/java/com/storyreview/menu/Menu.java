package com.storyreview.menu;

import com.storyreview.mlp.TFIDF;
import com.storyreview.database.DBHandler;
import com.storyreview.mlp.Classifier;

import java.util.List;
import java.util.Scanner;
//Defining colors for terminal
public class Menu {

    public static final String ANSI_RED = "\u001B[31m"; 
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    private TFIDF tfidf;
    private Classifier classifier;
    private DBHandler DBHandler;
// initializing TFIDF, Classifier, and DBHandler
    public Menu(TFIDF tfidf, Classifier classifier,DBHandler dbHandler) {
        this.tfidf = tfidf;
        this.classifier = classifier;
        this.DBHandler = dbHandler;
    }

    public void runMenu() {
        try (Scanner scanner = new Scanner(System.in)) { // Ensure Scanner is closed automatically
            boolean exit = false;

            while (!exit) {
                // Display menu options
                System.out.println(ANSI_BLUE + "Story Review Menu:" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "1. Classify a user story"+ ANSI_RESET);
                System.out.println(ANSI_GREEN + "2. Display TFIDF Information"+ ANSI_RESET);
                System.out.println(ANSI_GREEN + "3. Add a story to the database"+ ANSI_RESET);
                System.out.println(ANSI_GREEN + "4. Search Stories by emotional Label"+ ANSI_RESET);
                System.out.println(ANSI_GREEN + "5. Exit"+ ANSI_RESET);
                System.out.print(ANSI_YELLOW+ "Enter your choice (1-5): "+ ANSI_RESET);

                int choice;
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } else {
                    System.out.println(ANSI_RED+ "Invalid input. Please enter a number."+ ANSI_RESET); 
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
                        addStoryToDatabase(scanner);
                        break;
                    case 4:
                        searchStoriesByEmotion(scanner);
                        break;
                    case 5:
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
    private void addStoryToDatabase(Scanner scanner){
        System.out.println("Enter the text of the story:");
        String userStory = scanner.nextLine();

        if (tfidf == null || classifier == null){
            System.out.println("TFIDF or Classifier is not properly initialized.");
            return;
        }
        String sentiment = classifier.classifyUserStory(userStory, tfidf);
        DBHandler.addStory(userStory, sentiment);
    }
    private void searchStoriesByEmotion(Scanner scanner){
        System.out.print(ANSI_YELLOW + "Enter the emotional label: " + ANSI_RESET);
        String sentiment = scanner.nextLine().trim().toLowerCase();
       
        List<String> stories = DBHandler.getStoriesBySentiment(sentiment);

        if(stories.isEmpty()){
            System.out.println("No stories found with the sentiment: " + sentiment);
        }else{
            System.out.println("Stories with sentiment '" + sentiment + "':");
            for (String story : stories){
                System.out.println(story);
            }
        }
        }

    }


        
        
        //run a while loop until the user chooses to exit
        //each iteration, display the menu options and ask the user for a number representing their choice
        //use a switch statement to use the appropriate feature based on what the user selects
        //for the main mlp feature you just need to get another user input for the user's story and then run classifier.classifyUserStory(String userStoryThatYouJustGot,TFIDFProcessor); and it should work


