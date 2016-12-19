package com.mbcsonline.sss;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.mbcsonline.sss.map.HttpConnection;
import com.mbcsonline.sss.map.PathJSONParser;
import com.mbcsonline.sss.modelclass.Constant;
import com.mbcsonline.sss.modelclass.GPSData;
import com.mbcsonline.sss.modelclass.ServerRequest;
import com.mbcsonline.sss.modelclass.ServerResponse;
import com.mbcsonline.sss.utils.AsyncCommunicator;
import com.mbcsonline.sss.utils.AsyncTaskHandler;
import com.mbcsonline.sss.utils.Base64;
import com.mbcsonline.sss.utils.CustomDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks,  GoogleApiClient.OnConnectionFailedListener, AsyncCommunicator, LocationListener {

    final String TAG = "MapsActivity";
    CustomDialog dialog = null ;
    ArrayList<LatLng> homeToSchoolPoints = null;
    ArrayList<LatLng> schoolToHomePoints = null ;
    private int studentId = -1 ;
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    AsyncTaskHandler task = null ;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        Bundle bundle = getIntent().getExtras();

        studentId = Integer.parseInt( bundle.getString("studentId")) ;

        Toast.makeText(MapsActivity.this, "studentId : " + studentId , Toast.LENGTH_SHORT).show();
        ServerRequest request = new ServerRequest();
        dialog = new CustomDialog(this);
        request.setCommand(Constant.ACTION_GET_GPS);
        request.setStudentId(studentId);
        request.setDatetime("1970-01-01");
        String data = new Gson().toJson(request) ;
        task = new AsyncTaskHandler(this);
        task.execute(new String[]{Constant.GPS_URL,data} );
        // Initializing
        MarkerPoints = new ArrayList<>();


        MarkerPoints.add(new LatLng(40.722543,-73.998585));
        MarkerPoints.add(new LatLng(40.7057, -73.9964));
        MarkerPoints.add(new LatLng(40.7064, -74.0094));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager() .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


         //mapFragment.getMap();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                                       @Override
                                       public void onMapClick(LatLng point) {
                                           //move map camera
                                           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MarkerPoints.get(0),13));
                                           mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                                       }
                                   });

        init();

    }



    public void init(){

        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

//        ServerRequest request = new ServerRequest();
//        dialog = new CustomDialog(this);
//        request.setCommand(Constant.ACTION_GET_GPS);
//        request.setStudentId(studentId);
//        request.setDatetime("1970-01-01");
//        String data = new Gson().toJson(request) ;
//        AsyncTaskHandler task = new AsyncTaskHandler(this);
//        task.execute(new String[]{Constant.GPS_URL,data} );


        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MarkerPoints.get(0),13));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        addMarkers();
    }

    private String getMapsApiDirectionsUrl() {


        // Origin of route
        int origin =0;
        String str_origin = "origin=" + MarkerPoints.get(origin).latitude+","+MarkerPoints.get(origin).longitude;

        // Destination of route
        int dest =MarkerPoints.size()-1;
        String str_dest = "destination=" + MarkerPoints.get(dest).latitude+","+MarkerPoints.get(dest).longitude;

        // Sensor enabled
        String sensor = "sensor=false";



        String waypoints = "waypoints=optimize:true|";
        for(int i=0 ; i<MarkerPoints.size() ;i++){
            waypoints= waypoints + MarkerPoints.get(i).latitude + "," + MarkerPoints.get(i).longitude;
            if(i<MarkerPoints.size()-1){
                waypoints= waypoints  + "|";
            }
        }

        String params = str_origin + "&" + str_dest +"&"+ waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;



        return url;
    }

    private void addMarkers() {
        if (mMap != null) {

            for(int i=0 ; i<MarkerPoints.size() ;i++){
                String title =(i+1) +" Positon ";
                mMap.addMarker(new MarkerOptions().position(MarkerPoints.get(i)).title(title));
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    @Override
    public String getStringById(int id) {
        return this.getString(id);
    }

    @Override
    public Context getContext() {
        return this;
    }


    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = new PolylineOptions();

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.RED);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }

    @Override
    public void onProgressUpdate(String progress) {

    }

    @Override
    public void onProcessResult(String result) {
        task =null ;
        dialog.showMessage(getString(R.string.error_dialog_title) , "BALENDRSA");

        Log.v(TAG,result + "");
        ServerResponse response = new Gson().fromJson(result,ServerResponse.class );
        GPSData []data = null ;
        data = response.getGpsData();
       // dialog.showMessage(getString(R.string.info_dialog_title), "DATA LENGTH : " + data.length );

        if(response.isSuccess() == false) {
            dialog.showMessage(getString(R.string.error_dialog_title) , Base64.decode(response.getMessage()));
        }
        else{
            if(data != null && data.length > 0){
                int routeType = getSelectedRadioValue() ;
                homeToSchoolPoints = new ArrayList<>();
                schoolToHomePoints = new ArrayList<>();

                for (int i=0; i<data.length; i++){
                    GPSData g = data[i] ;
                    if (routeType == g.getRouteType()){
                        if (g.getLongitude() != null && g.getLatitude() != null ){
                            if(routeType == Constant.ROUTE_TYPE_HOME_TO_SCHOOL){
                                homeToSchoolPoints.add(new LatLng( Double.parseDouble( g.getLongitude()),Double.parseDouble( g.getLatitude()))) ;
                            }
                            else
                            {
                                schoolToHomePoints.add(new LatLng( Double.parseDouble( g.getLongitude()),Double.parseDouble( g.getLatitude()))) ;
                            }
                        }

                    }
                }
                PolylineOptions polyLineOptions = new PolylineOptions();
                MarkerPoints.clear();
                if (routeType == Constant.ROUTE_TYPE_HOME_TO_SCHOOL) {
                    if(homeToSchoolPoints != null && homeToSchoolPoints.size() > 1) {
                        polyLineOptions.addAll(homeToSchoolPoints);

                        MarkerPoints.add(homeToSchoolPoints.get(0));
                        MarkerPoints.add(homeToSchoolPoints.get(homeToSchoolPoints.size() - 1));
                        polyLineOptions.width(10);
                        polyLineOptions.color(Color.RED);
                        mMap.addPolyline(polyLineOptions);
                    }
                }else {
                    if(schoolToHomePoints != null && schoolToHomePoints.size() > 1) {
                        polyLineOptions.addAll(schoolToHomePoints);
                        MarkerPoints.add(schoolToHomePoints.get(0));
                        MarkerPoints.add(schoolToHomePoints.get(homeToSchoolPoints.size() - 1));
                        polyLineOptions.width(10);
                        polyLineOptions.color(Color.RED);
                        mMap.addPolyline(polyLineOptions);
                    }
                }
            }
            else{
                dialog.showMessage(getString(R.string.info_dialog_title) , Base64.decode(response.getMessage()));
            }
        }


    }

    private int getSelectedRadioValue(){
        int routeType = -1 ;
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rgBusRoute) ;
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId()) ;
        if(radioButton.getId() == R.id.rbHomeToSchool){
            routeType =1 ;
        }
        else if(radioButton.getId() == R.id.rbSchoolToHome)
        {
            routeType =2 ;
        }

        return routeType ;
    }

    public void cancelTask(){
        if (task!=null  ) {
            task.cancel(true);
            task = null ;
        }

    }
}