package com.jialincai.script_visualizer;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Scene implements IScene {
    
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    // Data fields
    int num;
    String header;
    String content;
    int sentiment;
    Set<String> characters = new TreeSet<>();
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    
    Scene(int num, String header, String content, Set<String> characters) {
        this.num = num;
        this.header = header;
        this.content = content;
        this.characters = characters;
        
        sentiment = analyzeSentiment(content);
    }
    
    @Override
    public Set<String> getCharacters() {
        return characters;
    }

    @Override
    public int getNumber() {
        return num;
    }
    
    @Override
    public String getHeader() {
        return header;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public int getSentiment() {
        return sentiment;
    }
    
    int analyzeSentiment(String text) {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,parse, sentiment");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object
        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);
        
        int sentiScore = 0;
        for (CoreSentence sentence : document.sentences()) {
            switch(sentence.sentiment().toLowerCase()) {
            case "very positive":
                sentiScore += 4;
                break;
            case "positive":
                sentiScore += 2;
                break;
            case "negative":
                sentiScore -= 1;
                break;
            case "very negative":
                sentiScore -= 2;
                break;
            default:
                sentiScore += 0;
            }
        }
        return sentiScore;
    }
    
    @Override
    public int compareTo(IScene scene) {
        return num - scene.getNumber();
    }

}
