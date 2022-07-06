package com.jialincai.script_visualizer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ScriptTest {
    
    static String scriptLink = "https://imsdb.com/scripts/Things-My-Father-Never-Taught-Me,-The.html";
    static Script script = new Script(scriptLink);
    
    @Test
    public void testImportScript() {
        String scriptTxt = script.getText();

        assertTrue(scriptTxt.contains("She burps loudly and gives a smile of satisfaction."));
    }
    
    @Test
    public void testExtractSceneHeaders() {
        List<String> sceneHeaders = script.extractSceneHeaders(script.importScript(scriptLink));
        
        assertEquals(sceneHeaders.get(0), "INT.   LIVING ROOM   -   NIGHT.");
        assertEquals(sceneHeaders.get(1), "EXT.   EAST PERTH STREETS    -    PRESENT DAY.");
        assertEquals(sceneHeaders.get(2), "INT.   DEPARTMENT STORE   -   DAY.");
    }
    
    @Test
    public void testGetLines() {
        String scriptTxt = script.getText();
        List<String> lines = script.getLines(scriptTxt);
        
        assertEquals(lines.get(0), "THE THINGS MY FATHER NEVER TAUGHT ME");
        assertEquals(lines.get(1), "Written by");
        assertEquals(lines.get(2), "Burleigh Smith");
    }
    
    @Test
    public void testExtractCharacterNames() {
        String content = script.scenes.get(8).getContent();
        Set<String> names = script.extractCharacterNames(content);
        
        assertEquals(names.size(), 2);
        assertTrue(names.contains("MARY"));
        assertTrue(names.contains("MELVIN"));
    }
    
    @Test
    public void testConstructScenes() {
        IScene scene = script.scenes.get(8);
        Set<String> names = scene.getCharacters();
        
        assertEquals(scene.getNumber(), 8);
        assertEquals(scene.getHeader(), "EXT.   CARPARK   -   DAY.");
        assertEquals(names.size(), 2);
        assertTrue(names.contains("MARY"));
        assertTrue(names.contains("MELVIN"));
        assertTrue(scene.getSentiment() > 0);
    }
    
    @Test
    public void testConstructCharacters() {
        assertEquals(script.characters.size(), 3);
        assertTrue(script.characters.get("MARY") != null);
        assertTrue(script.characters.get("MELVIN") != null);
        assertTrue(script.characters.get("ADULT MELVIN") != null);
    }
    
    @Test
    public void testConstructRelations() {
        IRelation r = script.relations.get("MARYMELVIN");
        
        assertEquals(1, script.relations.size());
        assertEquals(2, r.getWeight());
        assertTrue(r.getSentiment() > 0);
    }
    
    @Test
    public void testGetSceneCharacter() {
        Set<IScene> selectedScenes = script.getScene(new Character("MARY"));
        Iterator<IScene> iter = selectedScenes.iterator();
        
        assertEquals(2, selectedScenes.size());
        assertEquals("EXT.   CARPARK   -   DAY.", iter.next().getHeader());
        assertEquals("EXT.   PARK   -   DAY.", iter.next().getHeader());
    }
    
    @Test
    public void testGetSceneRelation() {
        Set<IScene> selectedScenes = script.getScene(new Relation("MELVIN", "MARY"));
        Iterator<IScene> iter = selectedScenes.iterator();
        
        assertEquals(2, selectedScenes.size());
        assertEquals("EXT.   CARPARK   -   DAY.", iter.next().getHeader());
        assertEquals("EXT.   PARK   -   DAY.", iter.next().getHeader());
    }
    
    @Test
    public void testExportJSON() {
        // Manually check that exported file is okay.
        assertTrue(script.exportJSON("JSON/test.json", 1));
    }
    
}
