/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PUTU
 */
public class TextFileReader {

    public static List<String> ReadAllLines(String filepath) throws FileNotFoundException, IOException {
        List<String> texts = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        try {
            String line = br.readLine();
            while (line != null) {
                if(!line.contains("//")){
                    texts.add(line);
                }
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return texts;
    }
    
    public static void main(String[] args) throws IOException{
        String filepath = "Data1.txt";
        List<String> texts = ReadAllLines(filepath);
        int counter = 1;
        for(String s: texts){
//            if(counter == 5){
//                break;
//            }
            System.out.println(counter + ". " + s);
            counter++;
        }
    }
}
