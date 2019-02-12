/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snsmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 *
 * @author PUTU
 */
public class TweetCrawler {

    static String consumarKey = "MKxInCNBL8BzaGqHEBBWqA";
    static String consumerSecret = "8x9msPy7Fl5LmyazMlaCPg0a1hKXOsvhJOirRCHIRk";

    double radius;
    String radiusUnit;
    String[] keywords;
    boolean geocoded = true;

    Twitter twitter;
    List<LocationDao> locationList;
    List<MessageDao> messageList;
    static int limit = 1000;

    public TweetCrawler(String[] keywords, double radius, String radiusUnit, Twitter twitter) {
        this.keywords = keywords;
        this.radius = radius;
        this.radiusUnit = radiusUnit;
        this.twitter = twitter;
        messageList = new ArrayList<>();
        loadLocation();
    }

    public TweetCrawler(String[] keywords, Twitter twitter) {
        this.keywords = keywords;
        this.geocoded = false;
        this.twitter = twitter;
        messageList = new ArrayList<>();
        loadLocation();
    }

    public void loadLocation() {
        MySQLConnector mysqlconn = new MySQLConnector();
        mysqlconn.connect();
        locationList = mysqlconn.getAllNewLocation();
        mysqlconn.close();
    }

    public List<Geocode> getAllGeocode(LocationDao location) {
        List<Geocode> geos = new ArrayList<>();
        String lines[] = location.geoloc.split("\\r?\\n");
        for (String line : lines) {
            String[] geo = line.split(" ");
            Geocode g = new Geocode(geo[0], geo[1]);
            geos.add(g);
        }
        return geos;
    }

    public int beginCrawl() throws TwitterException {
        int insertedMessage = 0;
        for (LocationDao location : locationList) {
            System.out.println("Querying: " + location.name);
            for (String searchKey : keywords) {
                System.out.println("Keyword: " + searchKey);
                List<Geocode> coords = getAllGeocode(location);
                for (Geocode geo : coords) {
                    Query query;
                    if (geocoded) {
                        query = new Query(searchKey).geoCode(new GeoLocation(geo.latitude, geo.longitude), radius, radiusUnit);
                    } else {
                        query = new Query(searchKey);
                    }
                    QueryResult result;
                    do {
                        result = twitter.search(query);
                        for (Status status : result.getTweets()) {
                            MessageDao msg = new MessageDao();
                            msg.id = status.getId();
                            msg.locid = location.id;
                            msg.text = status.getText();
                            msg.date = status.getCreatedAt();

                            if (status.getGeoLocation() != null) {
                                msg.latitude = status.getGeoLocation().getLatitude();
                                msg.longitude = status.getGeoLocation().getLongitude();
                            }

                            messageList.add(msg);
                            insertedMessage += MySQLConnector.addMessage(msg);
                        }
                    } while ((query = result.nextQuery()) != null);
                }
            }
        }
        return insertedMessage;
    }

    public int beginCrawlNonGeoTaggedMessage(int locID) throws TwitterException {
        int insertedMessage = 0;
        for (String searchKey : keywords) {
            Query query = new Query(searchKey);
            QueryResult result;
            do {
                result = twitter.search(query);
                for (Status status : result.getTweets()) {
                    MessageDao msg = new MessageDao();
                    msg.id = status.getId();
                    msg.locid = locID;
                    msg.text = status.getText();
                    msg.date = status.getCreatedAt();

                    if (status.getGeoLocation() == null) {
                        messageList.add(msg);
                        insertedMessage += MySQLConnector.addMessage(msg);
                    }

                }
            } while ((query = result.nextQuery()) != null);
        }

        return insertedMessage;
    }

    public static void main(String args[]) throws TwitterException, IOException {

        String token = "48695060-k1I5CQhmD71M28lD2v4Uoy2ct6TnopmHRTF97pXaV";
        String tokenSecret = "shmvDD2M8DWcXS3tWTF4rruIpa4w0UegjKB4XTIo0kkrf";

        TwitterFactory factory = new TwitterFactory();
        AccessToken accessToken = new AccessToken(token, tokenSecret);
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumarKey, consumerSecret);
        twitter.setOAuthAccessToken(accessToken);

        String[] keywords = {"traffic is", "in traffic"};
        double radius = 2;
        String radiusUnit = "km";
        TweetCrawler tweetTraffic = new TweetCrawler(keywords, radius, radiusUnit, twitter);
        int counter = tweetTraffic.beginCrawl();
        System.out.println("NumOfMessage: " + tweetTraffic.messageList.size());
        System.out.println("Geocoded Search: " + counter);

//        String[] keywords2 = {"LA Traffic"}; //locid = 60
//        String[] keywords3 = {"Santa Monica Fwy"}; //locid = 20
//        String[] keywords4 = {"San Gabriel River Fwy"}; //locid = 21
//        TweetCrawler nonGeocoded = new TweetCrawler(keywords4, twitter);
//        int nGcounter = nonGeocoded.beginCrawlNonGeoTaggedMessage(21);
//        System.out.println("Nongeocoded Search: " + nGcounter);
    }
}
