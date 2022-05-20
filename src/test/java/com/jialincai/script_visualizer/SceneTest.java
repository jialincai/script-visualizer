package com.jialincai.script_visualizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import com.jialincai.script_visualizer.Scene;

public class SceneTest {
    
    @Test
    public void testConstructor() {
        int num = 0;
        String header = "INT. BEDROOM - DAY";
        String content = "POLLY is a 12 year old girl.";
        Set<String> names = new TreeSet<String>(Arrays.asList("POLLY"));
        Scene s = new Scene(num, header, content, names);
        
        assertEquals(num, s.getNumber());
        assertEquals(header, s.getHeader());
        assertEquals(content, s.getContent());
        assertEquals(names.size(), s.getCharacters().size());
        
        Set<String> namesRet = s.getCharacters();
        for (String name : names) {
            assertTrue(namesRet.contains(name));
        }
    }
    
    @Test
    public void testCompareTo() {
        String header = "INT. BEDROOM - DAY";
        String content = "POLLY is a 12 year old girl.";
        Set<String> names = new TreeSet<String>(Arrays.asList("POLLY"));
        
        Scene s1 = new Scene(0, header, content, names);
        Scene s2 = new Scene(1, header, content, names);
        Scene s3 = new Scene(0, header, content, names);
        
        assertTrue(s1.compareTo(s2) < 0);
        assertTrue(s1.compareTo(s3) == 0);
        
    }
    
    @Test
    public void testAnalyzeSentiment() {
        String header = "INT. BEDROOM - DAY";
        String content = "Polly hates pancakes.";
        Set<String> names = new TreeSet<String>(Arrays.asList("POLLY"));
        
        Scene s = new Scene(0, header, content, names);
        assertTrue(s.getSentiment() < 0);
    }
}
