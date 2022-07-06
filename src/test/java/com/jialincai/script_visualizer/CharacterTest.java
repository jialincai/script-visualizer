package com.jialincai.script_visualizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterTest {
    
    @Test
    public void testConstructor() {
        String name = "JENNY";
        int count = 1;
        Character c = new Character(name);
        
        assertEquals(c.getName(), name);
        assertEquals(c.getCount(), count);
    }
    
    @Test
    public void testIncCount() {
        String name = "JENNY";
        int count = 2;
        Character c = new Character(name);
        
        c.incCount();
        assertEquals(c.getCount(), count);
    }
    
    @Test
    public void testCompareTo() {
        String name1 = "JENNY";
        String name2 = "BEN";
        Character c1 = new Character(name1);
        Character c2 = new Character(name2);
        Character c3 = new Character(name1);
        
        assertTrue(c1.compareTo(c2) > 0);
        assertTrue(c1.compareTo(c3) == 0);
    }
}
