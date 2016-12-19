package com.mbcsonline.sss.utils;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.mbcsonline.sss.R;
import com.mbcsonline.sss.modelclass.Constant;

public class AsyncTaskHandler extends android.os.AsyncTask<String, String, String> {

    private AsyncCommunicator communicator = null;
    ProgressDialog pDialog;
    NetworkUtility networkUtility = null ;
    String TAG = "AsyncTaskHandler" ;
    public AsyncTaskHandler(final AsyncCommunicator communicator) {
        this.communicator = communicator;
        pDialog = new ProgressDialog(communicator.getContext());
        pDialog.setTitle(R.string.async_title);
        pDialog.setIndeterminate(true);
        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.v(TAG, "CANCEL called");

            }
        });

        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.v(TAG, "DISMISSED called");
                communicator.cancelTask();
            }
        });
    }
 
    @Override
    protected void onPostExecute(String objects) {
        if(!objects.trim().equals(""))
            communicator.onProcessResult(objects);
        else
            communicator.onProcessResult(Constant.SERVER_RESPONDED_WITH_BLANK_RESPONSE_JSON);
        pDialog.cancel();
    }
 
    @Override
    protected String doInBackground( String... params ) {
        String serverUrl = params[0] ;

        String serverData = params[1] ;
        Log.v(TAG ,  serverUrl);
        Log.v(TAG , serverData);
        String response = "" ;

        try {
            if(isCancelled()!=true) {
                publishProgress(R.string.wait_checking_network_connection + "");

                boolean isNetworkReachable = networkUtility.isServerReachable(Constant.LOGIN_URL);
                Log.v(TAG, "" + isNetworkReachable);
                if (isNetworkReachable) {
                    publishProgress(R.string.wait_communicating_with_server + "");
                    response = networkUtility.sync(serverUrl, serverData);
                    Log.v(TAG, "" + "response:" + response);
                } else {
                    response = Constant.NETWORK_NOT_REACHABLE_JSON;
                }
            }
            else{
                response = Constant.SERVER_OPERATION_CANCELED;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG ,""+ "Exception : " + e.getMessage());
        }

        return response ;
    }
 
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        networkUtility = new NetworkUtility(communicator.getContext()) ;

    }
 
    @Override
    protected void onProgressUpdate(String... values) {
       // communicator.onProgressUpdate(values[0]);
        showDialog(Integer.parseInt(values[0]));

    }

    private void showDialog(int messageId ){

        pDialog.setMessage( communicator.getStringById( messageId ));


        pDialog.show();
    }

    private void changeDialogMessage(int messageId){
//        pDialog.setMessage( communicator.getStringById( messageId ));
    }

}