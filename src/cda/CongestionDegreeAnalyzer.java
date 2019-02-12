/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cda;

/**
 *
 * @author PUTU
 */
public class CongestionDegreeAnalyzer {
    
    public static void main(String[] args){
        double sA = 0; // total expression score of area
        double nA = 3; // number of message in area
        double aA = 0.5; // weighting level of area
        
        double sL = -80; // total expression score of line
        double nL = 4; // number of message in line
        double aL = 1; // weighting level of line
        
        double numberOfPoints = 15;
        double[] sPs = {-25, -25, -25, -25, -12.5, -8.3, -6.25, -5, -4.17, -3.57, -3.125, -2.78, -2.5, -5, -5};
        double aP = 1 / numberOfPoints; // weighting level of point
        
        double bigO = -10;
        
        double areaScore = aA * sA / nA;
        double lineScore = aL * sL / nL;
        
        double sP = 0; //total expression score in point level
        double nP = 3; //number of message in point level
        for(int i=0; i< numberOfPoints; i++){
            sP += sPs[i];
        }
        double poinScore = aP * sP / nP;
        
        double totalScore = (poinScore + lineScore + areaScore) / bigO;
        System.out.println("Total Score: " + totalScore);
        
        double congestionDegree;
        if(totalScore <= 0){
            congestionDegree = 0;
        }else if(totalScore >= 1){
            congestionDegree = 1;
        }else{
            congestionDegree = totalScore;
        }
        
        System.out.println("Congestion Degree: " + congestionDegree);
    }
}
