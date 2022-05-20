package com.jialincai.script_visualizer;

import java.util.Set;

/**
 * A collection of scene objects.
 * @author jialincai
 *
 */
public interface IScript {
    
    /**
     * Returns a list of all scenes containing a relation.
     * @param name A relation
     * @return A list of scenes containing this relation.
     */
    Set<IScene> getScene(IRelation relation);
    
    /**
     * Returns a list of all scenes containing character.
     * @param name A character.
     * @return A list of scenes containing character.
     */
    Set<IScene> getScene(ICharacter character);
    
    /**
     * Returns the number of scenes in this script.
     * @return The number of scenes in this script.
     */
    int getCount();
    
    /**
     * Returns the entire script as a string.
     * @return The entire script.
     */
    String getText();
}
