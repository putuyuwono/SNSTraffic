/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snsmanager;

import java.util.Date;

/**
 *
 * @author PUTU
 */
public class MessageDao {
    public long id;
    public int locid;
    public String text;
    public String expressionText;
    public int expressionScore;
    public Date date;
    public Double latitude;
    public Double longitude;
    
    public MessageDao(){}
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + id + "\n");
        sb.append("LocID: " + locid + "\n");
        sb.append("Text: " + text + "\n");
        sb.append("Date: " + date + "\n");
        sb.append("LAT: " + latitude + "\n");
        sb.append("LNG: " + longitude + "\n");
        return sb.toString();
    }
}
