package com.jialincai.script_visualizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Script implements IScript {
    
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    // Data fields
    Elements scriptElement;
    Map<Integer, IScene> scenes = new TreeMap<>();
    Map<String, ICharacter> characters = new TreeMap<>();
    Map<String, IRelation> relations = new TreeMap<>();
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    
    Script(String scriptLink) {
        scriptElement = importScript(scriptLink);
        scenes = constructScenes(scriptElement);
        characters = constructCharacters(scenes);
        relations = constructRelations(scenes);
    }
    
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    // HELPER METHODS for Script constructor
    
    /**
     * Splits a script into scenes.
     * @param script The script as an HTML element.
     * @return A chronological list of all script scenes.
     */
    Map<Integer, IScene> constructScenes(Elements scriptElement) {
        Map<Integer, IScene> scenes = new TreeMap<>();
        List<String> sceneHeaders = extractSceneHeaders(scriptElement);
        String scriptText = scriptElement.text();
        
        int pos = 0;
        for (int i = 0; i < sceneHeaders.size(); i++) {
            String header = sceneHeaders.get(i);
            int start;
            int end;
            String content;
            // Special case: last scene
            if (i == sceneHeaders.size() - 1) {
                start = scriptText.indexOf(header, pos) + header.length();
                content = scriptText.substring(start);
            } else {
                String nextHeader = sceneHeaders.get(i + 1);
                pos = start = scriptText.indexOf(header, pos) + header.length();
                pos = end = scriptText.indexOf(nextHeader, pos);
                content = scriptText.substring(start, end);
            } 
            scenes.put(i, new Scene(i, header, content, extractCharacterNames(content)));
        }
        
        return scenes;
    }
    
    /**
     * Returns a list of all the characters in this script.
     * @param script A list of all the scenes in this script.
     * @return A list of characters in lexicographic order.
     */
    Map<String, ICharacter> constructCharacters(Map<Integer, IScene> scenes) {
        Map<String, ICharacter> characters = new TreeMap<>();
        
        for (Map.Entry<Integer, IScene> e : scenes.entrySet()) {
            Set<String> names = e.getValue().getCharacters();
            for (String name : names) {
                if (characters.containsKey(name)) {
                    characters.get(name).incCount();
                } else {
                    characters.put(name, new Character(name));
                }
            }
        }
        return characters;
    }
    
    /**
     * Returns a list of all the relationships in this script.
     * @param script A list of all the scenes in this script.
     * @return A list of the relationships.
     */
    Map<String, IRelation> constructRelations(Map<Integer, IScene> scenes) {
        Map<String, IRelation> relations = new TreeMap<>();
        
        for (Map.Entry<Integer, IScene> e : scenes.entrySet()) {
            Set<String> names = e.getValue().getCharacters();
            int charCount = names.size();
            String[] namesArray = names.toArray(new String[charCount]);
            for (int i = 0; i < charCount; i++) {
                String name = namesArray[i];
                for (int j = i + 1; j < charCount; j++) {
                    String otherName = namesArray[j];
                    if (name.equals(otherName)) { continue; }
                    
                    String key = generateKey(name, otherName);
                    if (relations.containsKey(key)) {
                        IRelation r = relations.get(key);
                        r.incWeight();
                        r.addSentiment(e.getValue().getSentiment());
                    } else {
                        relations.put(key, new Relation(name, otherName));
                    }
                }
            }
        }
        return relations;
    }
    
    /**
     * Generates a string to identify the character pair.
     * @param name Name of first character.
     * @param otherName Name of second character.
     * @return A unique identifier string for character pair.
     */
    String generateKey(String name, String otherName) {
        String[] names = new Relation(name, otherName).getCharacters();
        return names[0] + names[1];
    }
    
    /**
     * Connect to imsdb.com and get script element.
     * @param scriptLink The HTML link of the desired script.
     * @return The script as an HTML element.
     */
    Elements importScript(String scriptLink) {  
        // Connect to imsdb.com
        Document htmlDoc;
        try {
        htmlDoc = Jsoup.connect(scriptLink).get();
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Unable to connect to imsdb.com.");
            return null;
        }
        
        // Extract script as string.
        Elements scriptElement = htmlDoc.getElementsByClass("scrtext");
        if (scriptElement.isEmpty()) {
            System.err.println("Link does not contain script.");
            return null;
        }
        if (scriptElement.size() > 1) {
            System.err.println("Link contains multiple scripts.");
            return null;
        }
        return scriptElement;
    }
    
    /**
     * Returns a list of all the scene headers in the script.
     * @param scriptElement The script as an HTML element.
     * @return A list of all the script's scene headers.
     */
    List<String> extractSceneHeaders(Elements scriptElement) {
        Elements boldLines = scriptElement.first().getElementsByTag("b");
        List<String> sceneHeaders = new ArrayList<String>();
        for (Element line : boldLines) {
            if (line.text().contains("EXT.") || line.text().contains("INT.")) {
                sceneHeaders.add(line.text());
            }
        }
        return sceneHeaders;
    }
    
    /**
     * Splits a script into lines.
     * Empty lines and leading/trailing whitespace are removed.
     * @param script The full script text.
     * @return A list of script lines.
     */
    List<String> getLines(String text) {
        List<String> lines = new ArrayList<>(Arrays.asList(text.split("\r\n\r\n")));
        
        ListIterator<String> iter = lines.listIterator();
        while (iter.hasNext()) {
            String line = iter.next();
            
            if (line.equals("") || line.contains(" OMIT")) {
                iter.remove();
                continue;
            }
            
            iter.set(line.trim().replaceAll("\r\n", " "));
        }
        
        return lines;
    }

    /**
     * Given the content of a scene, return a list of all characters present.
     * @param content The body text of a scene.
     * @return A set of all the characters present in the scene.
     */
    Set<String> extractCharacterNames(String content) {
        Set<String> names = new TreeSet<>();
        List<String> lines = getLines(content);
        
        for (String line : lines) {
            if (line.contains("          ")) {
                String name = line.split("          ")[0];
                if (isValidName(name)) {
                    names.add(cleanName(name));
                }
            }
        }
        return names;
    }
    
    /**
     * Returns true if the argument is all uppercase. False otherwise.
     * @param name A string.
     * @return True if the argument is all uppercase. False otherwise.
     */
    boolean isValidName(String name) {
        return name.toUpperCase().equals(name);
    }
    
    /**
     * Returns a copy of the name with parenthetical 
     * and leading/trailing whitespace removed.
     * @param name A string containing character name.
     * @return The name with parenthetical and leading/trailing whitespace removed.
     */
    String cleanName(String name) {
        name = name.replace("(CONT'D)", "");
        name = name.replace("(ADR)"   , "");
        name = name.replace("(O.S.)"  , "");
        name = name.replace("(V.O.)"  , "");
        name = name.trim();
        return name;
    }
    
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------

    @Override
    public Set<IScene> getScene(IRelation relation) {
        Set<IScene> selectScenes = new TreeSet<>();
        String[] targetCharacters = relation.getCharacters();
        
        for (Map.Entry<Integer, IScene> e : scenes.entrySet()) {
            Set<String> sceneCharacters = e.getValue().getCharacters();
            if (sceneCharacters.contains(targetCharacters[0]) && sceneCharacters.contains(targetCharacters[1])) {
                selectScenes.add(e.getValue());
            }
        }
        
        return selectScenes;
    }

    @Override
    public Set<IScene> getScene(ICharacter character) {
        Set<IScene> selectScenes = new TreeSet<>();
        
        for (Map.Entry<Integer, IScene> e : scenes.entrySet()) {
            if (e.getValue().getCharacters().contains(character.getName())) {
                selectScenes.add(e.getValue());
            }
        }
        
        return selectScenes;
    }

    @Override
    public int getCount() {
        return scenes.size();
    }

    @Override
    public String getText() {
        return scriptElement.text();
    }

}
