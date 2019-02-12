/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PUTU
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String s) {
        boolean result = true;
        if (s != null && !s.isEmpty()) {
            result = false;
        }

        return result;
    }
    
    public static List<String> tokenizeString(String input){
        List<String> result = new ArrayList<>();
        
        return result;
    } 
}
