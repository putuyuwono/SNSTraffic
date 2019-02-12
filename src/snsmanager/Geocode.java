/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snsmanager;

/**
 *
 * @author PUTU
 */
public class Geocode {
    public double latitude;
    public double longitude;
    
    public Geocode(){
        
    }
    
    public Geocode(double lat, double lng){
        this.latitude = lat;
        this.longitude = lng;
    }
    
    public Geocode(String lat, String lng){
        this.latitude = Double.valueOf(lat);
        this.longitude = Double.valueOf(lng);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(latitude);
        sb.append(" " + longitude);
        return sb.toString();
    }
}
