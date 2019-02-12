/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snsmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MySQLConnector {

    private Connection connection = null;
    public String serverAddr = "164.125.121.105:3306";
    public String dbName = "corrsns";
    public String username = "root";
    public String userpass = "my_password";

    public MySQLConnector() {

    }

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + serverAddr + "/" + dbName, username, userpass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<LocationDao> getAllLocation() {
        List<LocationDao> locationList = new ArrayList<>();

        LocationDao location = null;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT LOCID, LOCNAME, LOCLAT, LOCLNG FROM LOCATION");
            while (resultSet.next()) {
                location = new LocationDao();
                location.id = resultSet.getInt("LOCID");
                location.name = resultSet.getString("LOCNAME");
                location.latitude = resultSet.getDouble("LOCLAT");
                location.longitude = resultSet.getDouble("LOCLNG");
                locationList.add(location);
            }
        } catch (Exception ex) {
        }

        return locationList;
    }

    public List<LocationDao> getAllNewLocation() {
        List<LocationDao> locationList = new ArrayList<>();

        LocationDao location = null;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT LOCID, LOCNAME, LOCGEO FROM LOCATION WHERE LOCID >= 32 AND LOCID <= 44");
            while (resultSet.next()) {
                location = new LocationDao();
                location.id = resultSet.getInt("LOCID");
                location.name = resultSet.getString("LOCNAME");
                location.geoloc = resultSet.getString("LOCGEO");
                locationList.add(location);
            }
        } catch (Exception ex) {
        }

        return locationList;
    }

    public MessageDao getMessageByID(long id) {
        MessageDao msg = null;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            String query = "SELECT MSGID, LOCID, MSGTEXT, MSGDATE, MSGLAT, MSGLNG FROM MESSAGE WHERE MSGID = " + id;
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                msg = new MessageDao();
                msg.id = resultSet.getLong("MSGID");
                msg.locid = resultSet.getInt("LOCID");
                msg.text = resultSet.getString("MSGTEXT");
                msg.date = resultSet.getTimestamp("MSGDATE");
                msg.latitude = resultSet.getDouble("MSGLAT");
                msg.longitude = resultSet.getDouble("MSGLNG");
            }
        } catch (Exception ex) {
        }

        return msg;
    }

    public List<MessageDao> getMessageWhere(String whereClause) {
        List<MessageDao> msgList = new ArrayList<>();
        MessageDao msg = null;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            String query = "SELECT MSGID, LOCID, MSGTEXT, MSGEXPTEXT, MSGEXP, MSGDATE, MSGLAT, MSGLNG FROM MESSAGE " + whereClause;
            System.out.println("Query: " + query);
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                msg = new MessageDao();
                msg.id = resultSet.getLong("MSGID");
                msg.locid = resultSet.getInt("LOCID");
                msg.text = resultSet.getString("MSGTEXT");
                msg.expressionText = resultSet.getString("MSGEXPTEXT");
                msg.expressionScore = resultSet.getInt("MSGEXP");
                msg.date = resultSet.getTimestamp("MSGDATE");
                msg.latitude = resultSet.getDouble("MSGLAT");
                msg.longitude = resultSet.getDouble("MSGLNG");
                msgList.add(msg);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return msgList;
    }
    
    public static List<MessageDao> fetchMessagesWhere(String whereClause){
        MySQLConnector mysql = new MySQLConnector();
        mysql.connect();
        List<MessageDao> msgs = mysql.getMessageWhere(whereClause);
        mysql.close();
        return msgs;
    }

    public int insertMessage(MessageDao message) {
        int affectedRow = 0;
        PreparedStatement ps;
        try {
            MessageDao msg = getMessageByID(message.id);
            if (msg == null) {
                if (message.latitude != null && message.longitude != null) {
                    ps = connection.prepareStatement("INSERT INTO MESSAGE (MSGID, LOCID, MSGTEXT, MSGDATE, MSGLAT, MSGLNG) VALUES (?,?,?,?,?,?)");
                    ps.setLong(1, message.id);
                    ps.setInt(2, message.locid);
                    ps.setString(3, message.text);
                    ps.setTimestamp(4, new java.sql.Timestamp(message.date.getTime()));
                    ps.setDouble(5, message.latitude);
                    ps.setDouble(6, message.longitude);
                } else {
                    ps = connection.prepareStatement("INSERT INTO MESSAGE (MSGID, LOCID, MSGTEXT, MSGDATE) VALUES (?,?,?,?)");
                    ps.setLong(1, message.id);
                    ps.setInt(2, message.locid);
                    ps.setString(3, message.text);
                    ps.setTimestamp(4, new java.sql.Timestamp(message.date.getTime()));
                }
                affectedRow = ps.executeUpdate();
                System.out.println("Message inserted: " + message.id);
            }
            else{
//                System.out.println("Message already exist: " + msg.id);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return affectedRow;
    }

    public static int addMessage(MessageDao message) {
        int affRow = 0;
        MySQLConnector mysql = new MySQLConnector();
        mysql.connect();
        affRow = mysql.insertMessage(message);
        mysql.close();
        return affRow;
    }
    
    public int updateExpression(MessageDao message){
        int affRow = 0;
        String query = "";
        PreparedStatement ps = null;
        try {
            MessageDao msg = getMessageByID(message.id);
            if (msg == null) {
                System.out.println("Message not found: " + message.id);
            }else{
                query = "UPDATE MESSAGE SET MSGEXPTEXT = '" + message.expressionText + "', MSGEXP = "+ message.expressionScore +" WHERE MSGID = " + message.id;
//                System.out.println("Update: " + query);
                ps = connection.prepareStatement(query);
                affRow = ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return affRow;
    }
    
    public static int updateMessageExpression(MessageDao message){
        int affRow = 0;
        MySQLConnector mysql = new MySQLConnector();
        mysql.connect();
        affRow = mysql.updateExpression(message);
        mysql.close();
        return affRow;
    }

    public static void main(String[] argv) {
        MySQLConnector mysqlconn = new MySQLConnector();
        mysqlconn.connect();

        String whereClause = "WHERE LOCID = 11";
        List<MessageDao> msgList = mysqlconn.getMessageWhere(whereClause);
        for (MessageDao m : msgList) {
            System.out.println(m.toString());
        }
        mysqlconn.close();
    }

}
