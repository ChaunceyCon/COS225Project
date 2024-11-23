package com.storyreview.mlp;

import java.util.*;

import com.storyreview.story.*;

public class Classifier {
    
    //all instance vars are hashmaps where the first key is the context (positive or negative) and then the value is the corresponding data
    //chance of a story being positive or negative
    private HashMap<String,Double> conProb = new HashMap<String,Double>();
    //collection of all positive/negative Storys
    private HashMap<String,HashMap<String,Story>> conCollection = new HashMap<String,HashMap<String,Story>>();
    //HashMap listing how many times each word appears in the positive/negative collection
    private HashMap<String,HashMap<String,Integer>> conWordCounts = new HashMap<String,HashMap<String,Integer>>();
    //total number of words in each context
    private HashMap<String,Integer> conTotalWords = new HashMap<String,Integer>();
    //HashMap of log(# of occurences of String word in this context / # of total words in this context)
    private HashMap<String,HashMap<String,Double>> conWordRatios = new HashMap<String,HashMap<String,Double>>();

    public Classifier() {
        //fill conCollection with a HashMap for the two contexts
        HashMap<String,Story> posCollection = new HashMap<String,Story>();
        HashMap<String,Story> negCollection = new HashMap<String,Story>();
        conCollection.put("positive",posCollection);
        conCollection.put("negative",negCollection);
        //do the same for conWordCounts
        HashMap<String,Integer> posWordCounts = new HashMap<String,Integer>();
        HashMap<String,Integer> negWordCounts = new HashMap<String,Integer>();
        conWordCounts.put("positive",posWordCounts);
        conWordCounts.put("negative",negWordCounts);
        //and conWordRatios
        HashMap<String,Double> posWordRatios = new HashMap<String,Double>();    
        HashMap<String,Double> negWordRatios = new HashMap<String,Double>();
        conWordRatios.put("positive",posWordRatios);
        conWordRatios.put("negative",negWordRatios);
    }

    //updates conCollection, conWordCounts, and conTotalWords based on the Story provided
    public void processStory(Story s) {
        //get whether the Story is positive or negative to edit the right values
        String context = s.getSentiment();
        //add Story to the appropriate collection
        conCollection.get(context).put(s.getKey(),s);

        Scanner wordScanner = new Scanner(s.getFinStory());
        String word;
        while(wordScanner.hasNext()) {
            word=wordScanner.next();
            //add 1 to word's value in the appropriate wordCounts hashmap, or set it to 1 if word isn't in the hashmap yet
            conWordCounts.get(context).merge(word,1,(c,n) -> c+1);
            //add 1 to the appropriate context's total wordcount, or set it to 1 if it doesn't yet have a value
            conTotalWords.merge(context,1,(c,n) -> c+1);
        }
        wordScanner.close();
    }
}
