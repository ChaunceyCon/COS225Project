package com.storyreview.story;

import java.util.*;

public class Story {
    
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
        //determine sentiment by seeing whether the emotions are primarily positive or negative (need to categorize all the emotions first)
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

    public ArrayList<String> getEmotions() {
        return emotions;
    }
}
