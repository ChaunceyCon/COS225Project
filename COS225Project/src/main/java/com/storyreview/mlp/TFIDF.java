package com.storyreview.mlp;

import java.util.*;

public class TFIDF {

    //instance vars
    private HashMap<String,HashMap<String,Integer>> TFHash =  new HashMap<String,HashMap<String,Integer>>();
    private HashMap<String,Double> IDFHash = new HashMap<String,Double>();
    private HashSet<String> vocab = new HashSet<String>();
    //this is the number of stories in which each word appears
    private HashMap<String,Integer> wordFreq = new HashMap<String,Integer>();

    //gets all the necessary TFIDF information from a single story, to be run during processing
    public void processStory(String story,String title,int storyNum) {
        Scanner wordScanner = new Scanner(story);
        String word;
        HashMap<String,Integer> storyTF = new HashMap<String,Integer>();
        //look at each word in the story to set the appropriate TFIDF values
        while(wordScanner.hasNext()) {
            word=wordScanner.next();
            //add 1 to word's wordFreq if this is the first time seeing word in this story
            if(!storyTF.containsKey(word)) {
                wordFreq.merge(word,1,(c,n) -> c+1);
            }
            //add 1 to the current word's TF or set it to 1 if it doesn't exist yet
            storyTF.merge(word,1,(c,n) -> c+1);
            //add word to vocab
            vocab.add(word);
        }
        wordScanner.close();
        //add storyTF to the main TFHash w an id based off its title and number
        TFHash.put(""+storyNum+"-"+title,storyTF);
    }

    //calculates IDFHash based on the final values for Preprocessor.storyCount and this.wordFreq
    public void fillIDFHash(int finStoryCount) {
        double idfVal;
        for(String word : vocab) {
            idfVal = Math.log((double)(finStoryCount/wordFreq.get(word)));
            IDFHash.put(word,idfVal);
        }
    }

    
}