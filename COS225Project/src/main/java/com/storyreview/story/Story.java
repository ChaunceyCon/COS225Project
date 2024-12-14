package com.storyreview.story;

import java.util.*;

import com.storyreview.database.Preprocessor;

public class Story {
     // Instance variables for the story's title, initial and final story text, sentiment, number, and emotions
    private String title,iniStory,finStory,sentiment;
    private int number;
    private ArrayList<String> emotions = new ArrayList<String>();

    public Story(String t,String iS,String fS,String labels,int n) {
        title=t;
        iniStory=iS;
        finStory=fS;
        number = n;
        //fill emotions by parsing labels
        Scanner lScanner = new Scanner(labels);
        lScanner.useDelimiter("\\W");
        while(lScanner.hasNext()) {
            emotions.add(lScanner.next());
        }
        //determine sentiment by seeing whether the emotions are primarily positive or negative
        HashSet<String> posLabels = Preprocessor.getPosLabels();
        HashSet<String> negLabels = Preprocessor.getNegLabels();
        //tally up how many of this Story's labels are positive and negative
        int posCount=0;
        int negCount=0;
        for(String e : emotions) {
            if(posLabels.contains(e)) {
                posCount++;
            }
            else if(negLabels.contains(e)) {
                negCount++;
            }
        }
        //set sentiment based on whichever is higher, or to null if both are 0
        if(posCount==0&&negCount==0) {
            sentiment="";
        }
        else if(posCount>=negCount) {
            sentiment="positive";
        }
        else if(negCount>posCount) {
            sentiment="negative";
        }
        lScanner.close();
    }

    //get methods
    public String getTitle() {
        return title;
    }

    public String getIniStory() {
        return iniStory;
    }

    public String getFinStory() {
        return finStory;
    }

    public String getSentiment() {
        return sentiment;
    }

    public int getNumber() {
        return number;
    }

    public String getKey() {
        return ""+number+"-"+title;
    }

    public ArrayList<String> getEmotions() {
        return emotions;
    }
}
