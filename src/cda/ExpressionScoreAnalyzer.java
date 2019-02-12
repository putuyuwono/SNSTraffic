/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cda;

import java.io.IOException;
import java.util.List;
import snsmanager.MessageDao;
import snsmanager.MySQLConnector;
import snsmanager.TwitterNLP;

/**
 *
 * @author PUTU
 */
public class ExpressionScoreAnalyzer {
    public static void main(String[] args) throws IOException{
        //1. Fetch Messages from Database
        //2. Compute the expression score of each message
        //3. Write the expression score of each message to Database
        
        List<MessageDao> msgs = MySQLConnector.fetchMessagesWhere("WHERE LOCID >= 20");
        System.out.println("Message Count: " + msgs.size());
        int counter = 0;
        for(MessageDao msg: msgs){
            TwitterNLP nlp = new TwitterNLP(msg.text);
            msg.expressionScore = nlp.measureExpression();
            msg.expressionText = nlp.keywords.trim();
            counter += MySQLConnector.updateMessageExpression(msg);
        }
        System.out.println("Updated Message: " + counter);
    }
}
