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
public class LocationDao {
    public int id;
    public String name;
    public double latitude;
    public double longitude;
    public String geoloc;
    
    public LocationDao(){}
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + id + "\n");
        sb.append("Name: " + name + "\n");
        sb.append("LAT: " + latitude + "\n");
        sb.append("LNG: " + longitude + "\n");
        sb.append("GEOLOC" + geoloc + "\n");
        return sb.toString();
    }
}
