/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snsmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PUTU
 */
public class SNSAnalyzer {
    MessageDao message;
    
    int locationType = -1; // 0 = point, 1 = line, 2 = area
    String expression;
    String locationInfo;
    String originalContent;
    
    List<String> satisfaction;
    List<String> dissatisfaction;
    List<String> strongAnger;
    
    public SNSAnalyzer(MessageDao msg){
        this.message = msg;
    }
    
    public void analyze() throws IOException{
        this.originalContent = message.text;
        
        TwitterNLP tweet = new TwitterNLP(this.originalContent);
        tweet.analyze();
        
        if(this.message.latitude != null && this.message.longitude != null){
            this.locationType = 1; //point information.
        }else{
            // We must check the location information in the dictionary 
        }
        
        //Next we need to measure the expression score
        //1. Load dictionary
    }
    
    private void initExpressionDict() throws IOException{
        satisfaction = new ArrayList<>();
        dissatisfaction = new ArrayList<>();
        strongAnger = new ArrayList<>();
        
        List<String> lines = util.TextFileReader.ReadAllLines(util.StaticVariables.expressionDictionary);
    }
}
