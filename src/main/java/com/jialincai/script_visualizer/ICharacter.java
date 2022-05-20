package com.jialincai.script_visualizer;

public interface ICharacter extends Comparable<ICharacter> {
    
    /**
     * Returns this character's name.
     * @return This character's name.
     */
    String getName();
    
    /**
     * Returns the number of scenes containing this character.
     * @return The number of scenes containing this character.
     */
    int getCount();
    
    /**
     * Increments the number of scenes containing this character by one.
     */
    void incCount();
    
    /**
     * Checks if two characters have the same name. Otherwise order lexicographically.
     * @param scene The scene to be compared to.
     * @return  0 : two characters are the equal.
     *         -1 : this character is lexicographically less.
     *          1 : this character is lexicographically greater.
     */
    int compareTo(ICharacter character);
    
}
