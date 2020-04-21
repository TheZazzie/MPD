package com.example.coursework;
// Zarko Ivanov S1431661

import android.util.Pair;

import java.util.Date;

public class TrafficData {

    String title;
    String description;
    Date date;
    Date endDate;
    Pair<Double,Double> location;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date date) {
        this.endDate = date;
    }

    public Pair<Double, Double> getLocation() {
        return location;
    }

    public void setLocation(Pair<Double, Double> location) {
        this.location = location;
    }
}



/*
 ##### Current #####
<title>Traffic Scotland - Current Incidents</title>
<description>Current incidents on the road network e.g. accidents</description>

<title>M74 J7 (Larkhall) - B7019 - Accident</title>
<description>Lane 2 of the M74 Southbound between Junction 7 and Junction 8 is currently blocked due to a road traffic incident.  Motorists are advised to use caution on approach and allow extra time for their journey.</description>
<georss:point>55.7456666961935 -3.96320280983579</georss:point>
<pubDate>Fri, 20 Mar 2020 08:20:20 GMT</pubDate>

 #### ROADWORKS ####

<title>Traffic Scotland - Planned Roadworks</title>
<description>Future roadworks on the road network.</description>

<title>M6</title>
<description>
Start Date: Tuesday, 24 March 2020 - 00:00<br />End Date: Wednesday, 01 April 2020 - 00:00<br />TYPE : AWP Location : The M6 northbound between junctions J41 and J42 Lane Closures : Lanes 1, 2 a
</description>
<georss:point>54.7047810186083 -2.79225736783276</georss:point>
<pubDate>Tue, 24 Mar 2020 00:00:00 GMT</pubDate>

<title>A1</title>
<description>
Start Date: Friday, 20 March 2020 - 00:00<br />End Date: Monday, 30 March 2020 - 00:00<br />TYPE : AWP Location : The A1 southbound between the A191 and the junction with the A184 Reason : R
</description>
<georss:point>54.9735594551227 -1.69745001074991</georss:point>
<pubDate>Fri, 20 Mar 2020 00:00:00 GMT</pubDate>

<title>A19</title>
<description>
Start Date: Friday, 20 March 2020 - 00:00<br />End Date: Saturday, 21 March 2020 - 00:00<br />TYPE : AWP Location : The A19 southbound between the A191 and the junction with the A193 Reason :
</description>
<georss:point>55.0287062826195 -1.52290710169931</georss:point>
<pubDate>Fri, 20 Mar 2020 00:00:00 GMT</pubDate>

<title>A1(M)</title>
<description>
Start Date: Friday, 20 March 2020 - 00:00<br />End Date: Saturday, 21 March 2020 - 00:00<br />TYPE : AWP Location : The A1M southbound between junctions J63 and J61 Lane Closures : Lanes 1 and
</description>
<georss:point>54.8703901330789 -1.5675194079651</georss:point>
<pubDate>Fri, 20 Mar 2020 00:00:00 GMT</pubDate>
*/