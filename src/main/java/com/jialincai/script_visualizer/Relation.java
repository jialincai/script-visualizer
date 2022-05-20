package com.jialincai.script_visualizer;

import java.util.Arrays;

public class Relation implements IRelation {
    
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    // Data fields
    String[] characters = new String[2];
    int weight;
    int sentiment;
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------

    Relation(String name1, String name2) {
        if (name1.compareTo(name2) < 0) {
            characters[0] = name1;
            characters[1] = name2;
        } else {
            characters[0] = name2;
            characters[1] = name1;
        }
        
        weight = 1;
        sentiment = 0;
    }
    
    @Override
    public String[] getCharacters() {
        return Arrays.copyOf(characters, characters.length);
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void incWeight() {
        weight++;
    }

    @Override
    public void addSentiment(int value) {
        sentiment += value;
    }

    @Override
    public int getSentiment() {
        return sentiment;
    }

    @Override
    public int compareTo(IRelation relation) {
        String[] otherCharacters = relation.getCharacters();
        if (characters[0].compareTo(otherCharacters[0]) == 0) {
            return characters[1].compareTo(otherCharacters[1]);
        } else {
            return characters[0].compareTo(otherCharacters[0]);
        }
    }

}
