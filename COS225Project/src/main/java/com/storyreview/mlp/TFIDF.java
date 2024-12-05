package com.storyreview.mlp;

import java.util.*;

import com.storyreview.story.*;

public class TFIDF {

    //instance vars
    private HashMap<String,HashMap<String,Integer>> TFHash =  new HashMap<String,HashMap<String,Integer>>();
    private HashMap<String,Double> IDFHash = new HashMap<String,Double>();
    private HashSet<String> vocab = new HashSet<String>();
    //this is the number of stories in which each word appears
    private HashMap<String,Integer> wordFreq = new HashMap<String,Integer>();

    //gets all the necessary TFIDF information from a single story, to be run during processing
    public void processStory(Story s,boolean isDataStory) {
        Scanner wordScanner = new Scanner(s.getFinStory());
        String word;
        HashMap<String,Integer> storyTF = new HashMap<String,Integer>();
        //look at each word in the story to set the appropriate TFIDF values
        while(wordScanner.hasNext()) {
            word=wordScanner.next();
            //only update wordFreq and vocab if the Story is from the dataset, not the user
            if(isDataStory) {
                //add 1 to word's wordFreq if this is the first time seeing word in this story
                if(!storyTF.containsKey(word)) {
                    wordFreq.merge(word,1,(c,n) -> c+1);
                }
                //add word to vocab
                vocab.add(word);
            }
            //add 1 to the current word's TF or set it to 1 if it doesn't exist yet
            storyTF.merge(word,1,(c,n) -> c+1);
        }
        wordScanner.close();
        //add storyTF to the main TFHash w an id based off its title and number
        TFHash.put(s.getKey(),storyTF);
    }

    //calculates IDFHash based on the final values for Preprocessor.storyCount and this.wordFreq
    public void fillIDFHash(int finStoryCount) {
        double idfVal;
        for(String word : vocab) {
            idfVal = Math.log((double)(finStoryCount/wordFreq.get(word)));
            IDFHash.put(word,idfVal);
        }
    }

    //test method to print this class's HashMaps to make sure they're right
    //TEST ONLY, if you run this method after processing the whole raw dataset the print output will be stupidly long
    public void testPrint() {
        System.out.println(TFHash);
        System.out.println(wordFreq);
        System.out.println(IDFHash);

    }

    //gets the TFIDF value of a specified word and document
    public double getTFIDF(String word,Story s) {
        int TF = TFHash.get(s.getKey()).get(word);
        /*sometimes user stories will contain words not in the dataset,
        meaning they have no value in IDFHash and we can't say anything about what they mean.
        in this case, we'll just return 0 for the IDF value so nothing gets added to either score in classifyUserStory()*/
        double IDF;
        if(IDFHash.containsKey(word)) {
            IDF=IDFHash.get(word);
        }
        else {
            IDF=0;
        }
        return TF*IDF;
    }

    //method used to remove userStory from TFHash so classifyUserStory() can be run again
    public void trimTFHash(String key) {
        TFHash.remove(key);
    }

    //get methods
    public HashMap<String,HashMap<String,Integer>> getTFHash() {
        return TFHash;
    }

    public HashMap<String,Double> getIDFHash() {
        return IDFHash;
    }

    public HashSet<String> getVocab() {
        return vocab;
    }

    public HashMap<String,Integer> getWordFreq() {
        return wordFreq;
    }

  // New displayInfo() method to print out key information about the TFIDF object
  public void displayInfo() {
    System.out.println("TFIDF Information:");

    // Display TFHash
    System.out.println("TF Hash:");
    for (Map.Entry<String, HashMap<String, Integer>> entry : TFHash.entrySet()) {
        System.out.println("Story ID: " + entry.getKey() + " -> " + entry.getValue());
    }

    // Display IDFHash
    System.out.println("IDF Hash:");
    for (Map.Entry<String, Double> entry : IDFHash.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
    }

    // Display vocab
    System.out.println("Vocabulary:");
    for (String word : vocab) {
        System.out.println(word);
    }

    // Display word frequency
    System.out.println("Word Frequencies:");
    for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
    }
}
}