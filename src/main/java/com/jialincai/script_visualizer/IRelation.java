package com.jialincai.script_visualizer;

public interface IRelation extends Comparable<IRelation> {
    
    /**
     * Returns the two related characters.
     * @return List of two characters in lexicographical order.
     */
    String[] getCharacters();
    
    /**
     * Returns the number of scenes containing both characters.
     * @return The number of scenes containing both characters.
     */
    int getWeight();
    
    /**
     * Increments the number of scenes containing both characters by one.
     */
    void incWeight();
    
    /**
     * Adds to the sentiment shared between two characters.
     * @param num The value to be added.
     */
    void addSentiment(int value);
    
    /**
     * Returns the overall sentiment of this relation.
     * @return The sentiment of this relation.
     */
    int getSentiment();
    
    /**
     * Checks if another relation contain the same characters.
     * Otherwise order lexicographically.
     * @param scene The relation being compared to.
     * @return  0 : two relations contains the same characters.
     *         -1 : this relation is lexicographically less.
     *          1 : this relation is lexicographically greater.
     */
    int compareTo(IRelation relation);
    
}
