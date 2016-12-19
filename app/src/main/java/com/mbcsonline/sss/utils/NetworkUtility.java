package com.mbcsonline.sss.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class NetworkUtility {
    Context context ;


    public NetworkUtility(Context ctx){
        this.context = ctx ;
    }

    /**
     * Method check whether server is reachable or not
     * @param urlString
     * @return
     */
    public boolean isServerReachable(String urlString ){
        boolean isNetworkAvailable = false ;
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected == true){
                    URLConnection urlConnection = null ;
                    try {
                        URL url = new URL( urlString );
                        urlConnection = url.openConnection();
                        InputStream in = urlConnection.getInputStream();
                        isNetworkAvailable = true ;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
        }
        return isNetworkAvailable;
    }

    /**
     * Method used to make request to server and get server response
     * @param serverUrl
     * @param data
     * @return
     */
    public String sync( String serverUrl,String data ){
        URLConnection urlConnection = null ;
        String response = "" ;
        try {
            URL url = new URL( serverUrl );
            urlConnection = url.openConnection();
            urlConnection.setDoInput (true);
            urlConnection.setDoOutput (true);
            urlConnection.setUseCaches (false);
            urlConnection.setRequestProperty("Content-Type","application/json");

            urlConnection.connect();

            if(!data.trim().equals("")) {
                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
                dos.writeBytes(data);
                dos.flush();
                dos.close();
            }

            InputStream in = urlConnection.getInputStream();
            response = inputStreamToString(in);
            System.out.println("Response : " + response );

            //readStream(in);
        }catch(Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Method used to convert input stream to string
     * @param inputStream
     * @return
     */
    private String inputStreamToString( InputStream inputStream ) {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                response.append(line);
            }
        } catch (Exception exp) {

        }
        return response.toString();
    }

}
