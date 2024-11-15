package com.storyreview.review;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Read {

    // Simulating the NLP model classification (Placeholder)
    private static String classifyTone(String storyText) {
        // Simple tone classifier based on story text (this is a placeholder; actual NLP model would go here)
        if (storyText.contains("happy") || storyText.contains("good")) {
            return "Optimistic";
        } else if (storyText.contains("sad") || storyText.contains("bad")) {
            return "Pessimistic";
        } else {
            return "Neutral";  // Default if not classified
        }
    }

    public static void main(String[] args) {
        String inputFilePath = "final.txt";  // Input file containing the stories
        String outputFilePath = "classified_stories.txt";  // Output file for classified stories

        try {
            // Read all lines from the input file
            List<String> lines = Files.readAllLines(Paths.get(inputFilePath));

            // Placeholder for story text
            StringBuilder storyText = new StringBuilder();
            for (String line : lines) {
                storyText.append(line).append(" ");

                // Assuming each story is made of 5 sentences
                if (storyText.toString().split("\\.").length >= 5) {
                    // Classify the tone of the story
                    String tone = classifyTone(storyText.toString().trim());

                    // Output the story and its tone to the console
                    System.out.println("Story: " + storyText.toString().trim());
                    System.out.println("Tone: " + tone);
                    System.out.println();

                    // Write the classified story to the output file
                    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                        writer.write("Story: " + storyText.toString().trim());
                        writer.newLine();
                        writer.write("Tone: " + tone);
                        writer.newLine();
                        writer.newLine();
                    }

                    // Reset the story text for the next story
                    storyText.setLength(0);
                }
            }

            System.out.println("Stories have been classified and saved to " + outputFilePath);

        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
        }
    }
}
