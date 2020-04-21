package com.example.coursework;
// Zarko Ivanov S1431661

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {

    @SuppressLint("SimpleDateFormat")
    public List<TrafficData> parseData(String dataToParse) {
        List<TrafficData> trafficData = null;
        TrafficData data = null;
        try {
            System.out.println(dataToParse);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("channel")) {
                        trafficData = new ArrayList<>();

                    } else if (xpp.getName().equalsIgnoreCase("item")) {
                        data = new TrafficData();

                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                       if (data != null)
                            data.setTitle(xpp.nextText());

                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (data != null){
                            data.setDescription(xpp.nextText());
                            Matcher endDateMatcher = Pattern.compile("End Date: (.*)<br />").matcher(data.getDescription());
                            if(endDateMatcher.find()){
                                String toParse = endDateMatcher.group().substring(10);
                                toParse = toParse.substring(0,toParse.length()-6);
                                System.out.println(toParse);
                                data.setEndDate(new SimpleDateFormat("E, dd MMM yyyy - HH:mm").parse(toParse));
                            }
                        }
                    } else if (xpp.getName().equalsIgnoreCase("point")) {
                        if (data != null){
                            String locationString = xpp.nextText();
                            data.setLocation(new Pair<Double, Double>(Double.valueOf(locationString.split(" ")[0]),Double.valueOf(locationString.split(" ")[1])));
                        }
                    }else if (xpp.getName().equalsIgnoreCase("pubdate")){
                        String dateString = xpp.nextText();
                        @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z").parse(dateString);
                        assert data != null;
                        data.setDate(date);
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        assert trafficData != null;
                        assert data != null;
                        trafficData.add(data);
                    }
                }
                // Get the next event
                eventType = xpp.next();

            } // End of while

            //return alist;
        } catch (XmlPullParserException | IOException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return trafficData;

    }
}
