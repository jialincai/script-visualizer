package com.jialincai.script_visualizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jialincai.script_visualizer.Relation;

public class RelationTest {
    
    @Test
    public void testConstructor() {
        String name1 = "JEFFREY";
        String name2 = "ABBY";
        Relation r = new Relation(name1, name2);
        
        assertEquals(name2, r.getCharacters()[0]);
        assertEquals(name1, r.getCharacters()[1]);
        assertEquals(1, r.getWeight());
        assertEquals(0, r.getSentiment());
    }
    
    @Test
    public void testCompareTo() {
        String name1 = "ABBY";
        String name2 = "JEFFREY";
        String name3 = "ARYA";
        Relation r1 = new Relation(name1, name2);
        Relation r2 = new Relation(name1, name3);
        Relation r3 = new Relation(name1, name2);
        
        assertTrue(r1.compareTo(r2) > 0);
        assertTrue(r1.compareTo(r3) == 0);
    }
}
