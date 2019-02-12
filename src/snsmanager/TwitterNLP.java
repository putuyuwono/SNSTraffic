/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snsmanager;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PUTU
 */
public class TwitterNLP {
    String modelFilename = "dict/model.20120919";
    String[] locationToken = {"^"};
    String[] expressionToken = {"V", "A", "R"};
    
    String[] satisfactions = {"lovely", "happy", "okay", "great"};
    String[] dissatisfactions = {"bad", "worse", "jammed", "jam", "stuck", "much", "many", "stop", "stopped", "block", "blocked", "blocks", "hour", 
        "accident", "traffic", "late", "close", "closed"};
    String[] stronganger = {"crazy", "insane", "misery", "hate", "ridiculous", "unbelievable", "unreal", "dying", "hell", "bitch", "outrageous", "piss", "fiascos",
        "horrendous", "sick", "suck", "upsetting", "sucks", "killing", "terrible", "tired", "annoyed", "fuck", "fucking", "scary", "infuriating", "damn", "shittier", 
        "congestion", "10mph"
    };
    
    String[] rule1Words = {"no", "not"};
    String[] rule2Words = {"so", "very", "too", "seriously", "beyond"};
    
    boolean isRule1Expression = false;
    boolean isRule2Expression = false;
    
    int satisfactionScore = 5;
    int dissatisfactionScore = -5;
    int strongangerScore = -10;
    
    int totalExpressionScore = 0;
    public String keywords = "";
    
    String message;
    List<String> locationCandidate;
    List<String> expresssionCandidate;
    
    String[] areaName = {"LA", "Los Angeles"};
    String[] roadName = {"Santa Monica", "San Gabriel River", "San Gabriel"};
    
    public TwitterNLP(String msg){
        this.message = msg;
        locationCandidate = new ArrayList<String>();
        expresssionCandidate = new ArrayList<String>();
    }
    
    public void analyze() throws IOException{
        Tagger tagger = new Tagger();
        tagger.loadModel(modelFilename);
        List<TaggedToken> taggedTokens = tagger.tokenizeAndTag(message);
        
        for (TaggedToken token : taggedTokens) {
//            System.out.println(token.tag.charAt(0) + "\t" + token.token);
            for (String tag : locationToken) {
                if (token.tag.equals(tag)) {
                    locationCandidate.add(token.token);
                }
            }

            for (String tag : expressionToken) {
                if (token.tag.equals(tag)) {
                    expresssionCandidate.add(token.token);
                }
            }
        }
    }
    
    public int measureExpression() throws IOException{
        Tagger tagger = new Tagger();
        tagger.loadModel(modelFilename);
        List<TaggedToken> taggedTokens = tagger.tokenizeAndTag(message);
        int keywordScore;
        for (TaggedToken token : taggedTokens) {
            String word = token.token;
            
            //Get Keyword Score
            if(isExist(word, satisfactions)){ 
                keywordScore = satisfactionScore;
                keywords += word + " ";
            }
            else if(isExist(word, dissatisfactions)) {
                keywordScore = dissatisfactionScore;
                keywords += word + " ";
            }
            else if(isExist(word, stronganger)) {
                keywordScore = strongangerScore;
                keywords += word + " ";
            }
            else keywordScore = 0;

            //Expression Measurement
            if(isRule1Expression){
                totalExpressionScore += (-1) * keywordScore;
            }else if (isRule2Expression){
                totalExpressionScore += 2 * keywordScore;
            }else{
                totalExpressionScore += keywordScore;
            }
//            System.out.println("Keyword: " + word + " Score: " + keywordScore + " TotalScore: " + totalExpressionScore);
            
            //Pattern Check
            isRule1Expression = isExist(word, rule1Words);
            isRule2Expression = isExist(word, rule2Words);
        }
        return totalExpressionScore;
    }
    
    private boolean isExist(String word, String[] words){
        boolean result = false;
        for(String w: words){
            if(word.equalsIgnoreCase(w)){
                result = true;
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String text = "CA,Congestion, avg 10mph I-10/Santa Monica Fwy EB towards I-405/San Diego Fwy NB at I-4 - http://t.co/yWwdjhnDeb";        
        TwitterNLP tweet = new TwitterNLP(text);
        int score = tweet.measureExpression();
        System.out.println("Score: " + score);
        System.out.println("Keywords: " + tweet.keywords);
        
//        System.out.println("Location: ");
//        for (String s : tweet.locationCandidate) {
//            System.out.println(s);
//        }
//
//        System.out.println("\nExpression: ");
//        for (String s : tweet.expresssionCandidate) {
//            System.out.println(s);
//        }
    }

}
