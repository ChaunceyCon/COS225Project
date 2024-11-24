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
        double IDF = IDFHash.get(word);
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

}