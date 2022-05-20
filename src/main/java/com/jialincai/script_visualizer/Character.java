package com.jialincai.script_visualizer;

public class Character implements ICharacter {
    
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    // Data fields
    String name;
    int count;
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------

    Character(String name) {
        this.name = name;
        count = 1;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void incCount() {
        count++;
    }

    @Override
    public int compareTo(ICharacter character) {
        return name.compareTo(character.getName());
    }

}
