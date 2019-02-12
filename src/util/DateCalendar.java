/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author PUTU
 */
public class DateCalendar {
    public static void convertDateToTimestamp(Date date){
        System.out.println(date.getTime());
    }
    
    public static void convertTimestampToDate(long ts){
        System.out.println(new Date(ts));
    }
    
    public static void main(String[] args){
        Date myDate;
        
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-7:00"));        
        cal.set(2014, 4, 11, 16, 20, 0);
        myDate = cal.getTime();
        System.out.println(myDate);
        System.out.println(cal.getTimeInMillis());
        
        System.out.println("Adding 10 mins");
        cal.add(Calendar.MINUTE, 10);
        myDate = cal.getTime();
        System.out.println(myDate);
        System.out.println(cal.getTimeInMillis());
        
        System.out.println("Test");
        convertTimestampToDate(1399998097000L);
        convertTimestampToDate(1400473586000L);
        convertTimestampToDate(1399850936000L);
        convertTimestampToDate(1399850936000L);
        convertTimestampToDate(1399851060000L);
        convertTimestampToDate(1399867471000L);
        convertTimestampToDate(1399867530000L);
        convertTimestampToDate(1399867602000L);
        convertTimestampToDate(1399867643000L);
        //1399160770000
    }
}
