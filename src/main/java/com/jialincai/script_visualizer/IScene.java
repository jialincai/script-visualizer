package com.jialincai.script_visualizer;

import java.util.Set;

public interface IScene extends Comparable<IScene> {

    /**
     * Returns the names of the characters in this scene.
     * @return A list of characters in this scene.
     */
    Set<String> getCharacters();
    
    /**
     * Returns the scene number.
     * @return The unique positive integer associated with this scene.
     */
    int getNumber();
    
    /**
     * Returns the scene header.
     * @return The header of this scene.
     */
    String getHeader();
    
    /**
     * Returns the content of the scene as a string.
     * @return The scene content.
     */
    String getContent();
    
    /**
     * Returns the sentiment score of the dialogue and screen action of this scene.
     * -2 : very negative
     * -1 : negative
     *  0 : neutral
     *  1 : positive
     *  2 : very positive
     * @return The sentiment score.
     */
    int getSentiment();
    
    /**
     * Checks if two scenes share the same scene number. Otherwise sort in increasing order.
     * @param scene The scene to be compared to.
     * @return  0 : two scenes are the equal.
     *         -1 : this scene is less.
     *          1 : this scene is greater.
     */
    int compareTo(IScene scene);
}
