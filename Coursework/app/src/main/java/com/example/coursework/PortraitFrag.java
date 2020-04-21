package com.example.coursework;
// Zarko Ivanov S1431661

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.support.v4.app.*;

import android.provider.ContactsContract;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class PortraitFrag extends Fragment implements OnClickListener {


    private TextView rawDataDisplay;
    private Date travelDate;
    private Button startButton;
    private TextView travelDateElement;
    private DatePickerDialog.OnDateSetListener travelDateListener;
    // Traffic Scotland URLs
    private String roadworksUrlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String plannedRoadworksUrlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String currentIncidentsUrlSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private MapView gMapView;
    private GoogleMap gMap;
    private String textSave;
    private ArrayList<TrafficData> locations;

    //Saved instance state variables
    private int yearSave = 0;
    private int daySave = 0;
    private int monthSave = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            view = inflater.inflate(R.layout.landscape_fragment, container, false);
        else
            view = inflater.inflate(R.layout.portrait_fragment, container, false);

        if(savedInstanceState != null){
            textSave = savedInstanceState.getString("data");
            daySave = savedInstanceState.getInt("day");
            monthSave = savedInstanceState.getInt("month");
            yearSave = savedInstanceState.getInt("year");
            travelDateElement = (TextView) view.findViewById(R.id.travelDate);
            rawDataDisplay = (TextView) view.findViewById(R.id.rawDataDisplay);

            rawDataDisplay.setText(textSave);
            if (daySave != 0 && monthSave != 0 && yearSave != 0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    travelDate = sdf.parse(String.valueOf(yearSave) +"/"+String.valueOf(monthSave+1)+"/"+String.valueOf(daySave));
                    System.out.println(sdf.format(travelDate));
                    if(travelDate != null)
                        travelDateElement.setHint(sdf.format(travelDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startProgress();
            }
        }

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            System.out.println("Permissions error");
        }


        gMapView = (MapView) view.findViewById(R.id.map);
        gMapView.onCreate(savedInstanceState);
        gMapView.onResume();

        try{
            MapsInitializer.initialize((getActivity().getApplicationContext()));
        } catch (Exception e){
            e.printStackTrace();
        }

        gMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putString("data", textSave);
        outState.putInt("day",daySave);
        outState.putInt("month", monthSave);
        outState.putInt("year", yearSave);
    }


    @Override
    public void onStart() {
        super.onStart();
        rawDataDisplay = (TextView) getView().findViewById(R.id.rawDataDisplay);
        startButton = (Button) getView().findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        travelDateElement = (TextView) getView().findViewById(R.id.travelDate);

        travelDateElement.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        travelDateListener,
                        year,
                        month,
                        day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        travelDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                monthSave = month;
                daySave = dayOfMonth;
                yearSave = year;
                System.out.println("Setting day:" + daySave + " year:" + yearSave + " month:" + monthSave);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    travelDate = sdf.parse(String.valueOf(year) +"/"+String.valueOf(month+1)+"/"+String.valueOf(dayOfMonth));
                    if(travelDate != null)
                        travelDateElement.setHint(sdf.format(travelDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

    }

    public void onClick(View aview) {
        startProgress();
    }

    public void startProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(roadworksUrlSource,plannedRoadworksUrlSource,currentIncidentsUrlSource,travelDate)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {

        private String roadworksUrl;
        private String plannedRoadworksUrl;
        private String incidentsUrl;
        private Date travelDate;

        private List<TrafficData> roadworks;
        private List<TrafficData> plannedRoadworks;
        private List<TrafficData> currentIncidents;

        public Task(String roadworksUrl,String plannedRoadworksUrl,String incidentsUrl,Date travelDate) {
            this.travelDate = travelDate;
            this.roadworksUrl = roadworksUrl;
            this.plannedRoadworksUrl = plannedRoadworksUrl;
            this.incidentsUrl = incidentsUrl;
        }

        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            final StringBuilder toPutOnScreen;
            StringBuilder current;
            Log.e("MyTag", "in run");

            toPutOnScreen = new StringBuilder("");

            roadworks = getTrafficDataFromUrl(roadworksUrl);
            plannedRoadworks = getTrafficDataFromUrl(plannedRoadworksUrl);
            currentIncidents = getTrafficDataFromUrl(incidentsUrl);
            locations = new ArrayList<>();


            if(roadworks.size() > 0){
                current = new StringBuilder();

                for (TrafficData element : roadworks){
                    if(travelDate != null){
                        if(element.getEndDate() != null){
                            if (travelDate.compareTo(element.getDate()) >= 0 && travelDate.compareTo(element.getEndDate()) < 0 ) {
                                current.append(element.getTitle()).append(element.getDate()).append(" Scheduled to end on ").append(sdf.format(element.getEndDate())).append("\n\n");
                                locations.add(element);
                            }
                        }else{
                            if(travelDate.compareTo(element.getDate()) == 0){
                                locations.add(element);
                                current.append(element.getTitle()).append("\n");
                            }
                        }
                    }
                }
                if(current.length() > 0){
                    toPutOnScreen.append("Current roadworks on date specified are:\n").append(current.toString()).append("\n\n\n");
                }
            }

            if(plannedRoadworks.size() > 0){
                current = new StringBuilder();

                for (TrafficData element : plannedRoadworks){
                    if(travelDate != null){
                        if (travelDate.compareTo(element.getDate()) < 0 && TimeUnit.DAYS.convert( (Math.abs(element.getDate().getTime() - travelDate.getTime())),TimeUnit.MILLISECONDS) < 7){
                            current.append(element.getTitle()).append(" Scheduled to start on ").append(sdf.format(element.getDate())).append("\n\n");
                            locations.add(element);
                        }

                    }
                }
                if(current.length() > 0){
                    toPutOnScreen.append("Planned roadworks within a week of the day specified are:\n").append(current.toString()).append("\n\n\n");
                }
            }

            if(currentIncidents.size() > 0){
                current = new StringBuilder();

                for (TrafficData element : roadworks) {
                    if (travelDate != null) {
                        if (travelDate.compareTo(element.getDate()) == 0) {
                            current.append(element.getTitle()).append(" between ").append(element.getDate()).append(" and ").append(element.getEndDate()).append(":\n").append("\nAt Location: ").append(element.getLocation().toString()).append("\n\n");
                            locations.add(element);
                        }
                    }
                }

                if(current.length() > 0){
                    toPutOnScreen.append("Incidents on the day of travel are:\n").append(current.toString()).append("\n\n\n");
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    textSave = toPutOnScreen.toString();
                    rawDataDisplay.setText(toPutOnScreen.toString());
                    if (daySave != 0 && monthSave != 0 && yearSave != 0) {
                        for(int i = 0;i < locations.size();i++){
                            gMap.addMarker(new MarkerOptions().position(new LatLng(locations.get(i).getLocation().first,locations.get(i).getLocation().second)).title(locations.get(i).getTitle()).snippet(locations.get(i).getDescription()));
                        }
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locations.get(0).getLocation().first, locations.get(0).getLocation().second), 12));
                    }
                }
            });
        }

        private List<TrafficData> getTrafficDataFromUrl(String url){

            DataParser parser = new DataParser();
            StringBuilder result = new StringBuilder();
            try {

                Log.e("MyTag", "in try");
                URL aurl = new URL(url);
                URLConnection yc = aurl.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                in.readLine();
                String inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine);
                    Log.e("MyTag", inputLine);
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            return parser.parseData(result.toString());
        }

    }

}
