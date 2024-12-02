package com.storyreview.mlp;

import java.util.*;

import com.storyreview.database.*;
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

    //updates conCollection, conWordCounts, and conTotalWords based on the Story provided, returning boolean storySuccessfullyProcessed
    public boolean processStory(Story s) {
        //get whether the Story is positive or negative to edit the right values
        String context = s.getSentiment();
        //only attempt to process the story if it was successfully categorized from its labels
        if(context.equals("positive")||context.equals("negative")) {
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
            return true;
        }
        else {
            return false;
        }
    }

    //calculates final values for conProb and conWordRatios once all Storys have been processed
    public void finishProcessing() {
        //total number of documents 
        int classifiedDocCount = conCollection.get("positive").size()+conCollection.get("negative").size();
        //set conProb's two values
        double posProb = (conCollection.get("positive").size())/classifiedDocCount;
        double negProb = (conCollection.get("negative").size())/classifiedDocCount;
        conProb.put("positive",posProb);
        conProb.put("negative",negProb);

        double ratio;
        //fill posWordRatios
        for(String word : conWordCounts.get("positive").keySet()) {
            if(conWordCounts.get("positive").containsKey(word)) {
                ratio=Math.log((conWordCounts.get("positive").get(word)+1)/conTotalWords.get("positive"));
            }
            else {
                ratio=Math.log(1/conTotalWords.get("positive"));
            }
            conWordRatios.get("positive").put(word,ratio);
        }
        //fill negWordRatios
        for(String word : conWordCounts.get("negative").keySet()) {
            if(conWordCounts.get("negative").containsKey(word)) {
                ratio=Math.log((conWordCounts.get("negative").get(word)+1)/conTotalWords.get("negative"));
            }
            else {
                ratio=Math.log(1/conTotalWords.get("negative"));
            }
            conWordRatios.get("negative").put(word,ratio);
        }
    }

    public String classifyUserStory(String iniStory,TFIDF TFIDFProcessor) {
        //create a Story object from the user's story and run TFIDF.processStory on it to add it's TF HashMap to TFIDF.TFHash
        Story userStory = new Story("user",iniStory,Preprocessor.deformat(iniStory),"",0);
        TFIDFProcessor.processStory(userStory, false);

        //add to the positive and negative chances based on the wordRatios and TFIDFs of each word in the user's story
        Scanner wordScanner = new Scanner(userStory.getFinStory());
        String word;
        double posChance=Math.log(conProb.get("positive"));
        double negChance=Math.log(conProb.get("negative"));
        while(wordScanner.hasNext()) {
            word = wordScanner.next();
            //need to add code to only run the below if a conWordRatios value exists for word
            posChance+=conWordRatios.get("positive").get(word)*TFIDFProcessor.getTFIDF(word,userStory);
            negChance+=conWordRatios.get("negative").get(word)*TFIDFProcessor.getTFIDF(word,userStory);
        }
        //removes userStory's HashMap from TFHash so this method can be used again
        TFIDFProcessor.trimTFHash(userStory.getKey());
        if(negChance>posChance) {
            return "negative";
        }
        else {
            return "positive";
        }
    }
}
